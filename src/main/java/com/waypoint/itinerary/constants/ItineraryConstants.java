package com.waypoint.itinerary.constants;

public class ItineraryConstants {
  public static final String TRIP_URI = "/trips";
  public static final String USERNAME_PARAM_NAME = "username";
  public static final String USERNAME_URI_PARAM = "{" + USERNAME_PARAM_NAME + "}";
  public static final String VALIDATE_USERNAME_URI = "/validate/username/" + USERNAME_URI_PARAM;
  public static final String TRIP_ID_PARAM_NAME = "tripId";
  public static final String TRIP_ID_URI_PARAM = "{" + TRIP_ID_PARAM_NAME + "}";
  public static final String CREATE_USER_TRIP_MAPPING_URI =
      "/" + USERNAME_URI_PARAM + "/trips/" + TRIP_ID_URI_PARAM;
  public static final String DELETE_TRIP_URI =
      "/trips/" + USERNAME_URI_PARAM + "/" + TRIP_ID_URI_PARAM;
  public static final String PLACE_ID_PARAM_NAME = "placeId";
  public static final String PLACE_ID_URI_PARAM = "{" + PLACE_ID_PARAM_NAME + "}";
  public static final String DELETE_PLACE_URI = "/places/" + PLACE_ID_URI_PARAM;
  public static final String PLACE_URI = "/places";
  public static final String ACTIVITY_ID_PARAM_NAME = "activityId";
  public static final String ACTIVITY_ID_URI_PARAM = "{" + ACTIVITY_ID_PARAM_NAME + "}";
  public static final String DELETE_ACTIVITY_URI = "/activities/" + ACTIVITY_ID_URI_PARAM;
  public static final String ACTIVITY_URI = "/activities";
  public static final String DEF_USER_NAME = "user";
}
