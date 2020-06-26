package com.epam.esm.exception;

import java.time.ZonedDateTime;
import java.util.List;

/** Represents response body for all errors to be sent to the user */
public class ErrorResponse {
  /** HTTP response status */
  private int status;

  private List<String> messages;
  private ZonedDateTime time;

  public ErrorResponse() {}

  public ErrorResponse(int status, List<String> messages, ZonedDateTime time) {
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

  public ZonedDateTime getTime() {
    return time;
  }

  public void setTime(ZonedDateTime time) {
    this.time = time;
  }
}
