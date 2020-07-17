package com.epam.esm.repository;

import com.epam.esm.entity.Event;

/** Represents base event repository interface for storing events in the data storage. */
public interface EventRepository {

  /**
   * Persists given event in the repository.
   *
   * @param event event to persist
   * @return id of the successfully saved event
   */
  long create(Event event);
}
