package com.epam.esm.exception;
/**
 * Data access exception thrown when querying before prepared data from testing data
 * source doesn't return the expected result.
 */
public class TestingDatasourceException extends RuntimeException {
  public TestingDatasourceException() {}

  public TestingDatasourceException(String message) {
    super(message);
  }

  public TestingDatasourceException(String message, Throwable cause) {
    super(message, cause);
  }

  public TestingDatasourceException(Throwable cause) {
    super(cause);
  }

  public TestingDatasourceException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
