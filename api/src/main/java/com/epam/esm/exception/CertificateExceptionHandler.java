package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CertificateExceptionHandler {
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

//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> handleException(RuntimeException exception) {
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        errorResponse.setMessage(exception.getMessage());
//        errorResponse.setTime(LocalDateTime.now());
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
//       ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//        errorResponse.setMessage(exception.getMessage());
//        errorResponse.setTime(LocalDateTime.now());
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
}
