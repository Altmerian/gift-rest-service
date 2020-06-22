package com.epam.esm.exception;

/**
 * thrown when resource with certain parameters already exists in the data source
 */
public class ResourceConflictException extends Exception {
  public ResourceConflictException(String message) {
    super(message);
  }

  public ResourceConflictException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResourceConflictException(Throwable cause) {
    super(cause);
  }
}
