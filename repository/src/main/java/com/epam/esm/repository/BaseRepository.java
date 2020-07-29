package com.epam.esm.repository;

import com.epam.esm.entity.BaseEntity;
import com.epam.esm.specification.Specification;

import java.util.List;
import java.util.Optional;

/** Represents base repository interface  for common operations with entities stored in the data storage. */
public interface BaseRepository<E extends BaseEntity> {

  /**
   * Retrieves all persisted entities
   *
   * @return list of entities
   * @param page number of page to view
   * @param size number of entities per page
   */
  List<E> getAll(int page, int size);

  /**
   * Counts overall quantity of entities
   *
   * @return total amount of entities
   */
  long countAll();

  /**
   * Retrieves a entity with given id.
   *
   * @param id entity id
   * @return {@code Optional} containing entity with corresponding id and other parameters or {@code
   *     Optional.empty()} if entity with given id doesn't exist
   */
  Optional<E> get(long id);

  /**
   * Persists given entity in the repository.
   *
   * @param entity entity to persist
   * @return id of the successfully saved entity
   */
  long create(E entity);

  /**
   * Updates specified entity data stored in the repository.
   *
   * @param entity entity to be updated in the repository
   */
  void update(E entity);

  /**
   * Removes specified entity from the repository.
   *
   * @param entity entity to be removed from repository
   */
  void delete(E entity);

  /**
   * Makes a query for entities that match given criteria through {@code Specification}
   *
   * @param specification entity specification with necessary parameters
   * @return list of entities that match the specification
   */
  List<E> query(Specification<E> specification);
}
