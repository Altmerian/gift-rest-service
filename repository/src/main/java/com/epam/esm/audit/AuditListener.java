package com.epam.esm.audit;

import com.epam.esm.entity.BaseEntity;
import com.epam.esm.entity.Event;
import com.epam.esm.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Component
public class AuditListener {

  private EventRepository eventRepository;

  public AuditListener() {}

  @Autowired
  public void setEventRepository(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @PostPersist
  void afterPersist(BaseEntity entity) {
    createEvent(entity, Operation.INSERT);
  }

  @PostUpdate
  void afterUpdate(BaseEntity entity) {
    createEvent(entity, Operation.UPDATE);
  }

  @PostRemove
  void afterDelete(BaseEntity entity) {
    createEvent(entity, Operation.DELETE);
  }

  private void createEvent(BaseEntity entity, Operation operation) {
    Class<?> aClass = entity.getClass();
    Long entityId = entity.getId();

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    int userId = 0;
    if (principal instanceof Integer) {
      userId = (Integer) principal;
    }

    Event event = new Event();
    event.setOperation(operation);
    event.setEntityType(aClass.getSimpleName());
    event.setEntityId(entityId);
    event.setUserId((long) userId);
    eventRepository.create(event);
  }
}
