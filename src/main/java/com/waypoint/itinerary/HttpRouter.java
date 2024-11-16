package com.waypoint.itinerary;

import static com.waypoint.itinerary.constants.ItineraryConstants.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.waypoint.itinerary.web.ItineraryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HttpRouter {
  private final ItineraryHandler itineraryHandler;

  public HttpRouter(ItineraryHandler itineraryHandler) {
    this.itineraryHandler = itineraryHandler;
  }

  @Bean
  RouterFunction<ServerResponse> routes() {
    return route()
        .POST(TRIP_URI, RequestPredicates.accept(MediaType.APPLICATION_JSON), itineraryHandler)
        .DELETE(
            DELETE_TRIP_URI,
            RequestPredicates.accept(MediaType.APPLICATION_JSON),
            itineraryHandler::handleDeleteTrip)
        .POST(
            ACTIVITY_URI,
            RequestPredicates.accept(MediaType.APPLICATION_JSON),
            itineraryHandler::handleActivity)
        .DELETE(
            DELETE_ACTIVITY_URI,
            RequestPredicates.accept(MediaType.APPLICATION_JSON),
            itineraryHandler::handleDeleteActivity)
        .POST(
            PLACE_URI,
            RequestPredicates.accept(MediaType.APPLICATION_JSON),
            itineraryHandler::handleActivity)
        .DELETE(
            DELETE_PLACE_URI,
            RequestPredicates.accept(MediaType.APPLICATION_JSON),
            itineraryHandler::handleDeletePlace)
        .build();
  }
}
