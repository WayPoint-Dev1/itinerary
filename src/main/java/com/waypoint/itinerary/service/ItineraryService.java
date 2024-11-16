package com.waypoint.itinerary.service;

import static com.waypoint.itinerary.utilities.ItineraryMapper.getPlaceDTO;
import static com.waypoint.itinerary.utilities.ItineraryMapper.getTripDTO;

import com.waypoint.itinerary.client.UserMappingClient;
import com.waypoint.itinerary.domain.dto.ActivityDTO;
import com.waypoint.itinerary.domain.dto.PlaceDTO;
import com.waypoint.itinerary.domain.dto.PlaceMetaDTO;
import com.waypoint.itinerary.domain.dto.TripDTO;
import com.waypoint.itinerary.domain.entity.Place;
import com.waypoint.itinerary.domain.entity.PlaceActivityMap;
import com.waypoint.itinerary.domain.entity.Trip;
import com.waypoint.itinerary.domain.entity.TripPlaceMap;
import com.waypoint.itinerary.exception.ErrorMessage;
import com.waypoint.itinerary.exception.GenericException;
import com.waypoint.itinerary.repository.*;
import com.waypoint.itinerary.utilities.ItineraryMapper;
import java.util.Collections;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@Slf4j
public class ItineraryService {
  private final TripRepository tripRepository;
  private final PlaceRepository placeRepository;
  private final TripPlaceMapRepository tripPlaceMapRepository;
  private final ActivityRepository activityRepository;
  private final PlaceActivityMapRepository placeActivityMapRepository;
  private final PlaceMetaRepository placeMetaRepository;
  private final UserMappingClient userMappingClient;

  public ItineraryService(
      TripRepository tripRepository,
      UserMappingClient userMappingClient,
      PlaceRepository placeRepository,
      TripPlaceMapRepository tripPlaceMapRepository,
      ActivityRepository activityRepository,
      PlaceActivityMapRepository placeActivityMapRepository,
      PlaceMetaRepository placeMetaRepository) {
    this.tripRepository = tripRepository;
    this.userMappingClient = userMappingClient;
    this.placeRepository = placeRepository;
    this.tripPlaceMapRepository = tripPlaceMapRepository;
    this.activityRepository = activityRepository;
    this.placeActivityMapRepository = placeActivityMapRepository;
    this.placeMetaRepository = placeMetaRepository;
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
                            getPlaceDTO(
                                place, Collections.singletonList(updatedActivityDTO), null)));
  }

  public Mono<TripDTO> createPlace(PlaceDTO placeDTO) {
    return tripRepository
        .findByIdAndIsActive(placeDTO.getTripId(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.TRIP_NOT_FOUND)))
        .flatMap(
            trip ->
                placeRepository
                    .save(ItineraryMapper.getPlace(placeDTO))
                    .switchIfEmpty(
                        Mono.error(new GenericException(ErrorMessage.PLACE_CREATION_FAILED)))
                    .flatMap(place -> placeRepository.findById(place.getId()))
                    .flatMap(
                        place ->
                            tripPlaceMapRepository
                                .save(ItineraryMapper.getTripPlaceMap(trip.getId(), place.getId()))
                                .switchIfEmpty(
                                    Mono.error(
                                        new GenericException(
                                            ErrorMessage.TRIP_PLACE_MAPPING_SAVE_FAILED)))
                                .thenReturn(place))
                    .flatMap(
                        place ->
                            createPlaceMeta(place.getId(), placeDTO.getPlaceMeta())
                                .map(
                                    placeMetaDTO ->
                                        getPlaceDTO(place, Collections.emptyList(), placeMetaDTO)))
                    .map(
                        updatedPlaceDTO ->
                            getTripDTO(trip, Collections.singletonList(updatedPlaceDTO))));
  }

  public Mono<PlaceMetaDTO> createPlaceMeta(UUID placeId, PlaceMetaDTO placeMetaDTO) {
    return placeMetaRepository
        .save(ItineraryMapper.getPlaceMeta(placeId, placeMetaDTO))
        .switchIfEmpty(
            Mono.error(new GenericException(ErrorMessage.PLACE_METADATA_CREATION_FAILED)))
        .flatMap(placeMeta -> placeMetaRepository.findById(placeMeta.getId()))
        .map(ItineraryMapper::getPlaceMetaDTO);
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

  public Mono<TripDTO> updatePlace(PlaceDTO placeDTO) {
    return placeRepository
        .findByIdAndIsActive(placeDTO.getId(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.PLACE_NOT_FOUND)))
        .flatMap(
            place ->
                placeRepository
                    .save(ItineraryMapper.updatePlace(place, placeDTO))
                    .flatMap(
                        updatedPlace ->
                            updatePlaceMeta(updatedPlace.getId(), placeDTO.getPlaceMeta())
                                .map(
                                    placeMetaDTO ->
                                        getPlaceDTO(
                                            updatedPlace, Collections.emptyList(), placeMetaDTO))))
        .map(
            updatedPlace ->
                ItineraryMapper.getTripDTO(null, Collections.singletonList(updatedPlace)));
  }

  public Mono<PlaceMetaDTO> updatePlaceMeta(UUID placeId, PlaceMetaDTO placeMetaDTO) {
    return placeMetaRepository
        .findByPlaceIdAndIsActive(placeId, true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.PLACE_METADATA_NOT_FOUND)))
        .flatMap(
            placeMeta ->
                placeMetaRepository.save(ItineraryMapper.updatePlaceMeta(placeMeta, placeMetaDTO)))
        .map(ItineraryMapper::getPlaceMetaDTO);
  }

  public Mono<PlaceDTO> updatePlaceActivityMap(ActivityDTO activityDTO) {
    if (activityDTO.getPlaceId() != null) {
      return findExistingPlaceActivityMap(activityDTO)
          .switchIfEmpty(validateAndUpdatePlaceActivityMap(activityDTO));
    }
    return Mono.just(ItineraryMapper.getPlaceDTO(activityDTO));
  }

  private Mono<PlaceDTO> findExistingPlaceActivityMap(ActivityDTO activityDTO) {
    return placeActivityMapRepository
        .findByPlaceIdAndActivityIdAndIsActive(activityDTO.getPlaceId(), activityDTO.getId(), true)
        .map(placeActivityMap -> ItineraryMapper.getPlaceDTO(activityDTO));
  }

  private Mono<PlaceDTO> validateAndUpdatePlaceActivityMap(ActivityDTO activityDTO) {
    return placeRepository
        .findByIdAndIsActive(activityDTO.getPlaceId(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.PLACE_NOT_FOUND)))
        .flatMap(place -> findAndUpdatePlaceActivityMap(activityDTO, place));
  }

  private Mono<PlaceDTO> findAndUpdatePlaceActivityMap(ActivityDTO activityDTO, Place place) {
    return placeActivityMapRepository
        .findByActivityIdAndIsActive(activityDTO.getId(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.ACTIVITY_NOT_FOUND)))
        .flatMap(
            placeActivityMap ->
                placeActivityMapRepository
                    .save(
                        ItineraryMapper.updatePlaceActivityMap(
                            placeActivityMap, activityDTO.getId(), place.getId()))
                    .switchIfEmpty(
                        Mono.error(
                            new GenericException(ErrorMessage.PLACE_ACTIVITY_MAPPING_SAVE_FAILED)))
                    .thenReturn(getPlaceDTO(place, Collections.singletonList(activityDTO), null)));
  }

  public Mono<TripDTO> deleteTrip(Tuple2<String, UUID> request) {
    return tripRepository
        .findByIdAndIsActive(request.getT2(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.TRIP_NOT_FOUND)))
        .flatMap(this::deleteTripDetails)
        .map(tripDTO -> tripDTO.withUserName(request.getT1()))
        .flatMap(this::deleteUserTripMapping);
  }

  public Mono<ActivityDTO> deleteActivity(UUID activityId) {
    return placeActivityMapRepository
        .findByActivityIdAndIsActive(activityId, true)
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

  public Mono<PlaceDTO> deletePlace(UUID placeId) {
    return tripPlaceMapRepository
        .findByPlaceIdAndIsActive(placeId, true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.PLACE_NOT_FOUND)))
        .flatMap(this::deletePlaceDetails);
  }

  public Mono<PlaceMetaDTO> deletePlaceMeta(UUID placeId) {
    return placeMetaRepository
        .findByPlaceIdAndIsActive(placeId, true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.PLACE_METADATA_NOT_FOUND)))
        .flatMap(
            placeMeta ->
                placeMetaRepository
                    .save(ItineraryMapper.deletePlaceMeta(placeMeta))
                    .map(ItineraryMapper::getPlaceMetaDTO));
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
                                        .flatMap(
                                            updatedPlace ->
                                                deletePlaceMeta(updatedPlace.getId())
                                                    .map(
                                                        placeMetaDTO ->
                                                            getPlaceDTO(
                                                                updatedPlace,
                                                                activityList,
                                                                placeMetaDTO))))));
  }

  private Mono<ActivityDTO> deleteActivityDetails(PlaceActivityMap placeActivityMap) {
    return activityRepository
        .findByIdAndIsActive(placeActivityMap.getActivityId(), true)
        .switchIfEmpty(Mono.error(new GenericException(ErrorMessage.ACTIVITY_NOT_FOUND)))
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
                            .map(ItineraryMapper::getActivityDTO)
                            .map(
                                updatedActivityDTO ->
                                    updatedActivityDTO.withPlaceId(
                                        placeActivityMap.getPlaceId()))));
  }

  private Mono<TripDTO> deleteUserTripMapping(TripDTO tripDTO) {
    log.info("deleteTrip :: Initiate Delete User-Trip Mapping");
    return userMappingClient
        .deleteUserTripMapping(tripDTO.getUserName(), tripDTO.getId())
        .switchIfEmpty(
            Mono.error(new GenericException(ErrorMessage.USER_TRIP_MAPPING_DELETION_FAILED)))
        .map(userDTO -> tripDTO.withUserId(userDTO.getId()).withUserName(userDTO.getUserName()));
  }
}
