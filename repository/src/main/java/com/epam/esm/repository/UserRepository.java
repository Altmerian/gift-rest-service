package com.epam.esm.repository;

import com.epam.esm.entity.User;

/** Represents base user repository interface for common operations with data storage. */
public interface UserRepository extends BaseRepository<User>{

  /**
   * Checks whether repository contains given user.
   *
   * @param user user to check the presence
   * @return {@code true} if the repository contains specified user. Otherwise returns {@code false}
   */
  boolean contains(User user);
}
