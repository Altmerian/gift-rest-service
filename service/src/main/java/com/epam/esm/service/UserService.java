package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.repository.UserRepository;

import java.util.List;

/**
 * Represents an interface of service which interacts with the underlying repository layer for
 * user-related actions. An instance of user repository {@link UserRepository} should be aggregated
 * during implementation.
 */
public interface UserService {

  /**
   * Gets data of all users from the repository layer.
   *
   * @return list of users in certain transfer format
   * @param page number of page to view
   * @param size number of users per page
   */
  List<UserDTO> getAll(int page, int size);

  /**
   * Gets data of user with given id from the repository layer.
   *
   * @return user with given id in certain transfer format
   */
  UserDTO getById(long id);

  /**
   * Invokes repository method to persist user data in the system
   *
   * @param userDTO user data in transfer format
   * @return id of successfully persisted user
   */
  long create(UserDTO userDTO);

  /**
   * Invokes repository method to mark user as deleted in the system
   *
   * @param id id of the user to delete
   */
  void delete(long id);

  /**
   * Checks user data for duplicates in the system. Duplicate is a user with the same email.
   *
   * @param userDTO user data to search
   * @throws ResourceConflictException if user with given email already exists
   */
  void checkForDuplicate(UserDTO userDTO) throws ResourceConflictException;

  /**
   * Counts overall quantity of users in the system
   * @return total amount of users
   */
  long countAll();

  /**
   * Gets data of user with given email from the repository layer.
   *
   * @return user with given email in certain transfer format
   */
  UserDTO getByEmail(String email);
}
