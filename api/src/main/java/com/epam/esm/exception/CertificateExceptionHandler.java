package com.epam.esm.exception;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@ControllerAdvice
public class CertificateExceptionHandler {

  private static final Logger LOGGER = LogManager.getLogger();

  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException exception) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
    errorResponse.setMessage(exception.getMessage());
    errorResponse.setTime(LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({ResourceConflictException.class})
  public ResponseEntity<ErrorResponse> handleException(ResourceConflictException exception) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(HttpStatus.CONFLICT.value());
    errorResponse.setMessage(exception.getMessage());
    errorResponse.setTime(LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<ErrorResponse> handleException(HttpRequestMethodNotSupportedException exception) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
    errorResponse.setMessage(exception.getMessage());
    errorResponse.setTime(LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleException(MethodArgumentTypeMismatchException exception) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    errorResponse.setMessage("The request cannot be fulfilled due to bad syntax.");
    errorResponse.setTime(LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponse> handleAll(Exception exception) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), LocalDateTime.now());
    LOGGER.throwing(Level.ERROR, exception);
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
