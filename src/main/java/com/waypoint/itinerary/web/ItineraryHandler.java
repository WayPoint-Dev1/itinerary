package com.waypoint.itinerary.web;

import static com.waypoint.itinerary.utilities.ItineraryMapper.*;

import com.waypoint.itinerary.domain.dto.ActivityDTO;
import com.waypoint.itinerary.domain.dto.PlaceDTO;
import com.waypoint.itinerary.domain.dto.TripDTO;
import com.waypoint.itinerary.service.ItineraryService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItineraryHandler implements HandlerFunction<ServerResponse> {
  private final ItineraryService itineraryService;

  public ItineraryHandler(ItineraryService itineraryService) {
    this.itineraryService = itineraryService;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest request) {
    return request
        .bodyToMono(TripDTO.class)
        .flatMap(
            tripDTO -> {
              if (tripDTO.getId() == null) {
                return validateCreateTripRequest(tripDTO).flatMap(itineraryService::createTrip);
              } else {
                return validateUpdateTripRequest(tripDTO).flatMap(itineraryService::updateTrip);
              }
            })
        .flatMap(
            tripDTO ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(tripDTO));
  }

  public Mono<ServerResponse> handleDeleteTrip(ServerRequest serverRequest) {
    return validateDeleteTripRequest(serverRequest)
        .flatMap(itineraryService::deleteTrip)
        .flatMap(
            tripDTO ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(tripDTO));
  }

  public Mono<ServerResponse> handleActivity(ServerRequest request) {
    return request
        .bodyToMono(ActivityDTO.class)
        .flatMap(
            activityDTO -> {
              if (activityDTO.getId() == null) {
                return validateCreateActivityRequest(activityDTO)
                    .flatMap(itineraryService::createActivity);
              } else {
                return validateUpdateActivityRequest(activityDTO)
                    .flatMap(itineraryService::updateActivity)
                    .flatMap(itineraryService::updatePlaceActivityMap);
              }
            })
        .flatMap(
            placeDTO ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(placeDTO));
  }

  public Mono<ServerResponse> handleDeleteActivity(ServerRequest serverRequest) {
    return validateDeleteActivityRequest(serverRequest)
        .flatMap(itineraryService::deleteActivity)
        .flatMap(
            activityDTO ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(activityDTO));
  }

  public Mono<ServerResponse> handlePlace(ServerRequest request) {
    return request
        .bodyToMono(PlaceDTO.class)
        .flatMap(
            placeDTO -> {
              if (placeDTO.getId() == null) {
                return validateCreatePlaceRequest(placeDTO).flatMap(itineraryService::createPlace);
              } else {
                return validateUpdatePlaceRequest(placeDTO).flatMap(itineraryService::updatePlace);
              }
            })
        .flatMap(
            tripDTO ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(tripDTO));
  }

  public Mono<ServerResponse> handleDeletePlace(ServerRequest serverRequest) {
    return validateDeletePlaceRequest(serverRequest)
        .flatMap(itineraryService::deletePlace)
        .flatMap(
            activityDTO ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(activityDTO));
  }
}
