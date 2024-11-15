package com.waypoint.itinerary.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GenericException extends RuntimeException {
  private final ErrorMessage errorMessage;

  @Builder
  public GenericException(ErrorMessage errorMessage) {
    super(errorMessage.getMessageDescription());
    this.errorMessage = errorMessage;
  }

  public GenericException(String message) {
    super(message);
    this.errorMessage = null;
  }
}
