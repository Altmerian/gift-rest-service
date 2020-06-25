package com.epam.esm.exception;

/** Thrown when trying query nonexistent minor data from the repository. */
public class MinorResourceNotFoundException extends RuntimeException {

  public MinorResourceNotFoundException(String message) {
    super(message);
  }

  public MinorResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public MinorResourceNotFoundException(Throwable cause) {
    super(cause);
  }

  public <T> MinorResourceNotFoundException(Class<T> clazz,  long id) {
    super(String.format("%s with id=%d was not found.",clazz.getSimpleName(), id));
  }
}
