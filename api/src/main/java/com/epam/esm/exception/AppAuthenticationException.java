package com.epam.esm.exception;

import org.springframework.security.core.AuthenticationException;

/** Thrown when authentication is required but failed or absented. */
public class AppAuthenticationException extends AuthenticationException {

  public AppAuthenticationException(String message) {
    super(message);
  }

  public AppAuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

}
