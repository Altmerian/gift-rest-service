package com.epam.esm.exception;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** The main exception handler */
@ControllerAdvice
@ResponseBody
public class CertificateExceptionHandler {

  private static final Logger LOGGER = LogManager.getLogger();
  private static final org.slf4j.Logger slf4jLogger =  LoggerFactory.getLogger(CertificateExceptionHandler.class);

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleException(ResourceNotFoundException exception) {
    LOGGER.error(exception.getMessage());
    return createErrorResponse(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MinorResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(MinorResourceNotFoundException exception) {
    LOGGER.error(exception.getMessage());
    return createErrorResponse(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceConflictException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleException(ResourceConflictException exception) {
//    LOGGER.error(exception);
    slf4jLogger.error(exception.getMessage());
    return createErrorResponse(exception, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(NoHandlerFoundException exception) {
    LOGGER.error(exception);
    ErrorResponse errorResponse = createErrorResponse(exception, HttpStatus.BAD_REQUEST);
    errorResponse.setMessages(
        Collections.singletonList("The request cannot be fulfilled due to bad syntax."));
    return errorResponse;
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorResponse handleException(HttpRequestMethodNotSupportedException exception) {
    LOGGER.error(exception);
    return createErrorResponse(exception, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public ErrorResponse handleException(HttpMediaTypeNotSupportedException exception) {
    LOGGER.error(exception);
    return createErrorResponse(exception, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(ConstraintViolationException ex) {
    LOGGER.error(ex);
    List<String> messages =
        ex.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());
    ErrorResponse errorResponse = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    errorResponse.setMessages(messages);
    return errorResponse;
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(DataIntegrityViolationException exception) {
    LOGGER.error(exception);
    ErrorResponse errorResponse = createErrorResponse(exception, HttpStatus.BAD_REQUEST);
    errorResponse.setMessages(
        Collections.singletonList("Fields values violate data source constraints."));
    return errorResponse;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(MethodArgumentNotValidException ex) {
    LOGGER.error(ex);
    List<String> messages =
        ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
    ErrorResponse errorResponse = createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    errorResponse.setMessages(messages);
    return errorResponse;
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(HttpMessageNotReadableException exception) {
    LOGGER.error(exception);
    Throwable rootCause = ExceptionUtils.getRootCause(exception);
    JsonLocation location = new JsonLocation(new Object(), -1, -1, -1);
    String originalMessage = "";
    if (rootCause instanceof JsonProcessingException) {
      JsonProcessingException cause = (JsonProcessingException) rootCause;
      location = cause.getLocation();
      originalMessage = cause.getOriginalMessage();
    }
    ErrorResponse errorResponse = createErrorResponse(exception, HttpStatus.BAD_REQUEST);
    String rootMessage =
        String.format(
            "line: %d, column: %d",
            location.getLineNr(), location.getColumnNr());
    errorResponse.setMessages(
        Arrays.asList("Invalid JSON format; " + rootMessage, originalMessage));
    return errorResponse;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleAll(Exception exception) {
    LOGGER.error("Internal server error.", exception);
    ErrorResponse errorResponse = createErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    errorResponse.setMessages(Collections.singletonList("Oops. Something was going wrong"));
    return errorResponse;
  }

  private ErrorResponse createErrorResponse(Exception exception, HttpStatus status) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(status.value());
    errorResponse.setMessages(Collections.singletonList(exception.getMessage()));
    errorResponse.setTime(LocalDateTime.now().atZone(ZoneId.systemDefault()));
    return errorResponse;
  }
}
