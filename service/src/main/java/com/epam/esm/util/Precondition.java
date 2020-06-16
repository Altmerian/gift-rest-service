package com.epam.esm.util;

import com.epam.esm.exception.ResourceNotFoundException;

import java.util.Optional;

public class Precondition {
  public static <T> T checkExistence(Optional<T> resourceOptional) {
    if (!resourceOptional.isPresent()) {
      throw new ResourceNotFoundException("Resource with given id has not been found");
    }
    return resourceOptional.get();
  }
}
