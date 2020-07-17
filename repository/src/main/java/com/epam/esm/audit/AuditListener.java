package com.epam.esm.audit;

import com.epam.esm.entity.Event;
import com.epam.esm.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.lang.reflect.Field;

@Component
public class AuditListener {

  private EventRepository eventRepository;

  public AuditListener() {}

  @Autowired
  public AuditListener(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @PostPersist
  void afterPersist(Object object) throws Exception {
    createEvent(object, Operation.INSERT);
  }

  @PostUpdate
  void afterUpdate(Object object) throws Exception {
    createEvent(object, Operation.UPDATE);
  }

  @PostRemove
  void afterDelete(Object object) throws Exception {
    createEvent(object, Operation.DELETE);
  }

  private <T> void createEvent(Object object, Operation operation)
      throws NoSuchFieldException, IllegalAccessException {
    Class<?> aClass = object.getClass();
    @SuppressWarnings("unchecked")
    T entity = (T) object;

    Field idField = aClass.getDeclaredField("id");
    idField.setAccessible(true);
    Long entityId = (Long) idField.get(entity);

    Integer userId =
        (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Event event = new Event();
    event.setOperation(operation);
    event.setEntityType(aClass.getSimpleName());
    event.setEntityId(entityId);
    event.setUserId((long) userId);
    eventRepository.create(event);
  }
}
