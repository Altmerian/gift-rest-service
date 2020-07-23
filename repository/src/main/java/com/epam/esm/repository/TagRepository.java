package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

/** Represents base tag repository interface for common operations with data storage. */
public interface TagRepository extends BaseRepository<Tag> {

  /**
   * Checks whether repository contains given tag.
   *
   * @param tag tag to check the presence
   * @return {@code true} if the repository contains specified tag. Otherwise returns {@code false}
   */
  boolean contains(Tag tag);
}
