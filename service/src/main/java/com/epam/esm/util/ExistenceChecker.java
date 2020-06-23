package com.epam.esm.util;

import com.epam.esm.exception.ResourceNotFoundException;

import java.util.Optional;

/**
 * Contains methods which check if a value is present inside given {@link Optional} and perform
 * actions depending on the result
 */
public class ExistenceChecker {

  /**
   * Attempts to retrieve value from {@code Optional} and throws exception if none
   *
   * @param resourceOptional {@code Optional} to get from
   * @param <T> class of the resource
   * @return value of the {@code Optional} if it is present or throws {@link
   *     ResourceNotFoundException} otherwise
   */
  public static <T> T check(Optional<T> resourceOptional) {
    if (!resourceOptional.isPresent()) {
      throw new ResourceNotFoundException("Resource with given id has not been found");
    }
    return resourceOptional.get();
  }
}
