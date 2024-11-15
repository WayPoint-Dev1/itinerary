package com.waypoint.itinerary.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
  MANDATORY_FIELDS_MISSING_OR_INVALID(
      422, "MANDATORY_FIELDS_MISSING_OR_INVALID", "MANDATORY FIELDS MISSING/INVALID IN PAYLOAD"),
  USER_TRIP_MAPPING_SAVE_FAILED(500, "USER_TRIP_MAPPING_SAVE_FAILED", "ERROR WHILE SAVING USER TRIP MAPPING"),
  PLACE_ACTIVITY_MAPPING_SAVE_FAILED(500, "PLACE_ACTIVITY_MAPPING_SAVE_FAILED", "ERROR WHILE SAVING PLACE ACTIVITY MAPPING"),
  PLACE_ACTIVITY_MAPPING_DELETION_FAILED(500, "PLACE_ACTIVITY_MAPPING_DELETION_FAILED", "ERROR WHILE DELETING PLACE ACTIVITY MAPPING"),
  USER_TRIP_MAPPING_DELETION_FAILED(500, "USER_TRIP_MAPPING_DELETION_FAILED", "ERROR WHILE DELETING USER TRIP MAPPING"),
  USERNAME_NOT_FOUND(422, "USERNAME_NOT_FOUND", "PROVIDED USERNAME DOESN'T EXIST"),
  TRIP_NOT_FOUND(422, "TRIP_NOT_FOUND", "PROVIDED TRIP-ID DOESN'T EXIST"),
  ACTIVITY_NOT_FOUND(422, "ACTIVITY_NOT_FOUND", "PROVIDED ACTIVITY DOESN'T EXIST"),
  PLACE_NOT_FOUND(422, "PLACE_NOT_FOUND", "PROVIDED PLACE DOESN'T EXIST"),
  ACTIVITY_CREATION_FAILED(500, "ACTIVITY_CREATION_FAILED", "ERROR WHILE CREATING ACTIVITY"),
  TRIP_CREATION_FAILED(500, "TRIP_CREATION_FAILED", "ERROR WHILE CREATING TRIP"),
  ACTIVITY_DELETION_FAILED(500, "ACTIVITY_DELETION_FAILED", "ERROR WHILE DELETING ACTIVITY"),;
  private final int httpStatusCode;
  private final String messageCode;
  private final String messageDescription;

  ErrorMessage(int httpStatusCode, String messageCode, String messageDescription) {
    this.httpStatusCode = httpStatusCode;
    this.messageCode = messageCode;
    this.messageDescription = messageDescription;
  }
}