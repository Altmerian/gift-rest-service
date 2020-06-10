package com.epam.esm.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RestPreconditions {
  public static <T> T checkFound(T resource) {
    if (resource == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, resource.getClass().getSimpleName() + " not found");
    }
    return resource;
  }

  public static <T> T checkDuplicate(T resource) {
    if (resource == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, resource.getClass().getSimpleName() + " not found");
    }
    return resource;
  }
}
