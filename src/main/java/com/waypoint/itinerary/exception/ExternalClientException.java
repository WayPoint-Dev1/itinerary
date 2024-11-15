package com.waypoint.itinerary.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExternalClientException extends RuntimeException {
  private final int httpStatusCode;
  private final String messageCode;
  private final String messageDescription;

  @Builder
  public ExternalClientException(
      int httpStatusCode, String messageCode, String messageDescription) {
    super(messageCode);
    this.httpStatusCode = httpStatusCode;
    this.messageCode = messageCode;
    this.messageDescription = messageDescription;
  }
}
