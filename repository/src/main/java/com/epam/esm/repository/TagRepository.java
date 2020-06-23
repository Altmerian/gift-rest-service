package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.specification.Specification;

import java.util.List;
import java.util.Optional;

/** Represents base tag repository interface for common operations with data storage. */
public interface TagRepository {

  /**
   * Retrieves all persisted tags
   *
   * @return list of tags
   */
  List<Tag> getAll();

  /**
   * Retrieves a tag with given id.
   *
   * @param id tag id
   * @return {@code Optional} containing tag with corresponding id and other parameters or {@code
   *     Optional.empty()} if tag with given id doesn't exist
   */
  Optional<Tag> get(long id);

  /**
   * Persists given certificate in the repository.
   *
   * @param tag tag to persist
   * @return id of the successfully saved tag
   */
  long create(Tag tag);

  /**
   * Removes specified tag from the repository.
   *
   * @param tag tag to be removed from repository
   */
  void delete(Tag tag);

  /**
   * Makes a query for tags that match given criteria through {@code TagSpecification}
   *
   * @param specification tag specification with necessary parameters
   * @return list of tags that match the specification
   */
  List<Tag> query(Specification specification);

  /**
   * Checks whether repository contains given tag.
   *
   * @param tag tag to check the presence
   * @return {@code true} if the repository contains specified tag. Otherwise returns {@code false}
   */
  boolean contains(Tag tag);
}
