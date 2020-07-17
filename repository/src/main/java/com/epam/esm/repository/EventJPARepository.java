package com.epam.esm.repository;

import com.epam.esm.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

@Repository
@Transactional
public class EventJPARepository implements EventRepository {

  private final EntityManagerFactory entityManagerFactory;

  @Autowired
  public EventJPARepository(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public long create(Event event) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(event);
    entityManager.flush();
    entityManager.getTransaction().commit();
    return event.getId();
  }
}
