package com.epam.esm.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents response body for all errors to be sent to the user
 */
public class ErrorResponse {
  /**
   * HTTP response status
   */
  private int status;
  private List<String> messages;

  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime time;

  public ErrorResponse() {}

  public ErrorResponse(int status, List<String> messages, LocalDateTime time) {
    this.status = status;
    this.messages = messages;
    this.time = time;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public List<String> getMessages() {
    return messages;
  }

  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  public LocalDateTime getTime() {
    return time;
  }

  public void setTime(LocalDateTime time) {
    this.time = time;
  }
}
