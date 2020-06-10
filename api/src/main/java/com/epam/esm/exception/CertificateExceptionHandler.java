package com.epam.esm.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CertificateExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException exception) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
    errorResponse.setMessage(exception.getMessage());
    errorResponse.setTime(LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(ResourceConflictException exception) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(HttpStatus.CONFLICT.value());
    errorResponse.setMessage(exception.getMessage());
    errorResponse.setTime(LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @Override
  @NonNull
  public ResponseEntity<Object> handleHttpMessageNotReadable(
      @NonNull HttpMessageNotReadableException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    errorResponse.setMessage("The request cannot be fulfilled due to bad syntax.");
    errorResponse.setTime(LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
