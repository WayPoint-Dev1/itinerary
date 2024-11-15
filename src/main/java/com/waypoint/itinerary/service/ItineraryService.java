package com.waypoint.itinerary.service;

import com.waypoint.itinerary.client.UserMappingClient;
import com.waypoint.itinerary.domain.dto.ActivityDTO;
import com.waypoint.itinerary.domain.dto.PlaceDTO;
import com.waypoint.itinerary.domain.dto.TripDTO;
import com.waypoint.itinerary.domain.entity.PlaceActivityMap;
import com.waypoint.itinerary.domain.entity.Trip;
import com.waypoint.itinerary.domain.entity.TripPlaceMap;
import com.waypoint.itinerary.exception.ErrorMessage;
import com.waypoint.itinerary.exception.GenericException;
import com.waypoint.itinerary.repository.*;
import com.waypoint.itinerary.utilities.ItineraryMapper;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ItineraryService {
  private final TripRepository tripRepository;
  private final PlaceRepository placeRepository;
  private final TripPlaceMapRepository tripPlaceMapRepository;
  private final ActivityRepository activityRepository;
  private final PlaceActivityMapRepository placeActivityMapRepository;
  private final UserMappingClient userMappingClient;

  public ItineraryService(
      TripRepository tripRepository,
      UserMappingClient userMappingClient,
      PlaceRepository placeRepository,
      TripPlaceMapRepository tripPlaceMapRepository,
      ActivityRepository activityRepository,
      PlaceActivityMapRepository placeActivityMapRepository) {
    this.tripRepository = tripRepository;
    this.userMappingClient = userMappingClient;
    this.placeRepository = placeRepository;
    this.tripPlaceMapRepository = tripPlaceMapRepository;
    this.activityRepository = activityRepository;
    this.placeActivityMapRepository = placeActivityMapRepository;
  }

  public Mono<TripDTO> createTrip(TripDTO tripDTO) {
    return userMappingClient
        .validateUserName(tripDTO.getUserName())
        .flatMap(
            userDTO1 ->
                !userDTO1.isAvailable()
                    ? Mono.just(userDTO1)
                    : Mono.error(new GenericException(ErrorMessage.USERNAME_NOT_FOUND)))
        .then(
            tripRepository
                .save(ItineraryMapper.getTrip(tripDTO))
                .flatMap(
                    trip ->
                        tripRepository
                            .findById(trip.getId())
                            .switchIfEmpty(
                                Mono.error(
                                    new GenericException(ErrorMessage.TRIP_CREATION_FAILED))))
                .map(ItineraryMapper::getTripDTO)
                .flatMap(
                    tripDTO1 -> {
                      log.info("createTrip :: Initiate User-Trip Mapping");
                      return userMappingClient
                          .createUserTripMapping(tripDTO.getUserName(), tripDTO1.getId())
                          .switchIfEmpty(
                              Mono.error(
                                  new GenericException(ErrorMessage.USER_TRIP_MAPPING_SAVE_FAILED)))
                          .map(
                              userTripMapDTO ->
                                  tripDTO1
                                      .withUserId(userTripMapDTO.getUserId())
                                      .withUserName(tripDTO.getUserName()));
                    }));
  }

  public Mono<PlaceDTO> createActivity(ActivityDTO activityDTO) {
    return placeRepository
        .findByIdAndIsActive(activityDTO.getPlaceId(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.PLACE_NOT_FOUND)))
        .flatMap(
            place ->
                activityRepository
                    .save(ItineraryMapper.getActivity(activityDTO))
                    .switchIfEmpty(
                        Mono.error(new GenericException(ErrorMessage.ACTIVITY_CREATION_FAILED)))
                    .flatMap(activity -> activityRepository.findById(activity.getId()))
                    .flatMap(
                        activity ->
                            placeActivityMapRepository
                                .save(
                                    ItineraryMapper.getPlaceActivityMap(
                                        activityDTO.getId(), place.getId()))
                                .switchIfEmpty(
                                    Mono.error(
                                        new GenericException(
                                            ErrorMessage.PLACE_ACTIVITY_MAPPING_SAVE_FAILED)))
                                .thenReturn(activity))
                    .map(ItineraryMapper::getActivityDTO)
                    .map(
                        updatedActivityDTO ->
                            ItineraryMapper.getPlaceDTO(
                                place, Collections.singletonList(updatedActivityDTO))));
  }

  public Mono<TripDTO> updateTrip(TripDTO tripDTO) {
    return tripRepository
        .findByIdAndIsActive(tripDTO.getId(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.TRIP_NOT_FOUND)))
        .flatMap(trip -> tripRepository.save(ItineraryMapper.updateTrip(trip, tripDTO)))
        .map(ItineraryMapper::getTripDTO);
  }

  public Mono<ActivityDTO> updateActivity(ActivityDTO activityDTO) {
    return activityRepository
        .findByIdAndIsActive(activityDTO.getId(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.ACTIVITY_NOT_FOUND)))
        .flatMap(
            activity ->
                activityRepository.save(ItineraryMapper.updateActivity(activity, activityDTO)))
        .map(ItineraryMapper::getActivityDTO);
  }

  public Mono<TripDTO> deleteTrip(TripDTO tripDTO) {
    return tripRepository
        .findByIdAndIsActive(tripDTO.getId(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.TRIP_NOT_FOUND)))
        .flatMap(this::deleteTripDetails)
        .flatMap(trip -> deleteUserTripMapping(tripDTO, trip));
  }

  public Mono<ActivityDTO> deleteActivity(ActivityDTO activityDTO) {
    return placeActivityMapRepository
        .findByActivityIdAndIsActive(activityDTO.getId(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.ACTIVITY_NOT_FOUND)))
        .flatMap(this::deleteActivityDetails);
  }

  private Mono<TripDTO> deleteTripDetails(Trip trip) {
    return tripPlaceMapRepository
        .findByTripIdAndIsActive(trip.getId(), true)
        .flatMap(this::deletePlaceDetails)
        .collectList()
        .flatMap(
            placeList ->
                tripRepository
                    .save(ItineraryMapper.deleteTrip(trip))
                    .map(updatedTrip -> ItineraryMapper.getTripDTO(updatedTrip, placeList)));
  }

  private Mono<PlaceDTO> deletePlaceDetails(TripPlaceMap tripPlaceMap) {
    return placeRepository
        .findByIdAndIsActive(tripPlaceMap.getPlaceId(), true)
        .flatMap(
            place ->
                placeActivityMapRepository
                    .findByPlaceIdAndIsActive(place.getId(), true)
                    .flatMap(this::deleteActivityDetails)
                    .collectList()
                    .flatMap(
                        activityList ->
                            tripPlaceMapRepository
                                .save(ItineraryMapper.deleteTripPlaceMap(tripPlaceMap))
                                .then(
                                    placeRepository
                                        .save(ItineraryMapper.deletePlace(place))
                                        .map(
                                            updatedPlace ->
                                                ItineraryMapper.getPlaceDTO(
                                                    updatedPlace, activityList)))));
  }

  private Mono<ActivityDTO> deleteActivityDetails(PlaceActivityMap placeActivityMap) {
    return activityRepository
        .findByIdAndIsActive(placeActivityMap.getActivityId(), true)
        .flatMap(
            activity ->
                placeActivityMapRepository
                    .save(ItineraryMapper.deletePlaceActivityMap(placeActivityMap))
                    .switchIfEmpty(
                        Mono.error(
                            new GenericException(
                                ErrorMessage.PLACE_ACTIVITY_MAPPING_DELETION_FAILED)))
                    .then(
                        activityRepository
                            .save(ItineraryMapper.deleteActivity(activity))
                            .switchIfEmpty(
                                Mono.error(
                                    new GenericException(ErrorMessage.ACTIVITY_DELETION_FAILED)))
                            .map(ItineraryMapper::getActivityDTO)));
  }

  private Mono<TripDTO> deleteUserTripMapping(TripDTO tripDTO, TripDTO tripDTO1) {
    log.info("deleteTrip :: Initiate Delete User-Trip Mapping");
    return userMappingClient
        .deleteUserTripMapping(tripDTO.getUserName(), tripDTO1.getId())
        .switchIfEmpty(
            Mono.error(new GenericException(ErrorMessage.USER_TRIP_MAPPING_DELETION_FAILED)))
        .map(userDTO -> tripDTO1.withUserId(userDTO.getId()).withUserName(userDTO.getUserName()));
  }
}
