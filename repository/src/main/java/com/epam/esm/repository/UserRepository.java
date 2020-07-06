package com.epam.esm.repository;

import com.epam.esm.entity.User;
import com.epam.esm.specification.Specification;

import java.util.List;
import java.util.Optional;

/** Represents base user repository interface for common operations with data storage. */
public interface UserRepository {

  /**
   * Retrieves all persisted users
   *
   * @return list of users
   * @param page number of page to view
   * @param size number of users per page
   */
  List<User> getAll(int page, int size);

  /**
   * Counts overall quantity of users
   *
   * @return total amount of users
   */
  long countAll();

  /**
   * Retrieves a user with given id.
   *
   * @param id user id
   * @return {@code Optional} containing user with corresponding id and other parameters or {@code
   *     Optional.empty()} if user with given id doesn't exist
   */
  Optional<User> get(long id);

  /**
   * Persists given certificate in the repository.
   *
   * @param user user to persist
   * @return id of the successfully saved user
   */
  long create(User user);

  /**
   * Removes specified user from the repository.
   *
   * @param user user to be removed from repository
   */
  void delete(User user);

  /**
   * Makes a query for users that match given criteria through {@code Specification}
   *
   * @param specification user specification with necessary parameters
   * @return list of users that match the specification
   */
  List<User> query(Specification<User> specification);

  /**
   * Checks whether repository contains given user.
   *
   * @param user user to check the presence
   * @return {@code true} if the repository contains specified user. Otherwise returns {@code false}
   */
  boolean contains(User user);
}
