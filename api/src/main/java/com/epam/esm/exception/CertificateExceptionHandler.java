package com.epam.esm.exception;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Set;

@ControllerAdvice
@ResponseBody
public class CertificateExceptionHandler {

  private static final Logger LOGGER = LogManager.getLogger();

  @ExceptionHandler({ResourceNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleException(ResourceNotFoundException exception) {
    return createErrorResponse(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({ResourceConflictException.class})
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleException(ResourceConflictException exception) {
    return createErrorResponse(exception, HttpStatus.CONFLICT);
  }

  @ExceptionHandler({NoHandlerFoundException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(NoHandlerFoundException exception) {
    ErrorResponse errorResponse = createErrorResponse(exception, HttpStatus.BAD_REQUEST);
    errorResponse.setMessage("The request cannot be fulfilled due to bad syntax.");
    return errorResponse;
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorResponse handleException(HttpRequestMethodNotSupportedException exception) {
    return createErrorResponse(exception, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public ErrorResponse handleException(HttpMediaTypeNotSupportedException exception) {
    return createErrorResponse(exception, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(ConstraintViolationException ex) {
    Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
    StringBuilder strBuilder = new StringBuilder();
    for (ConstraintViolation<?> violation : violations) {
      strBuilder.append(violation.getMessage()).append("\n");
    }
    ErrorResponse errorResponse = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    errorResponse.setMessage(strBuilder.toString());
    return errorResponse;
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(MethodArgumentNotValidException ex) {
    StringBuilder strBuilder = new StringBuilder();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      strBuilder
          .append(error.getField())
          .append(": ")
          .append(error.getDefaultMessage())
          .append(" \n ");
    }
    ErrorResponse errorResponse = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    errorResponse.setMessage(strBuilder.toString());
    return errorResponse;
  }

  @ExceptionHandler({Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleAll(Exception exception) {
    LOGGER.throwing(Level.ERROR, exception);
    return createErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse createErrorResponse(Exception exception, HttpStatus status) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(status.value());
    errorResponse.setMessage(exception.getMessage());
    errorResponse.setTime(LocalDateTime.now());
    return errorResponse;
  }
}
