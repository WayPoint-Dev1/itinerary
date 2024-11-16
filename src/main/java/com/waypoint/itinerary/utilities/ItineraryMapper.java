package com.waypoint.itinerary.utilities;

import static com.waypoint.itinerary.constants.ItineraryConstants.DEF_USER_NAME;
import static com.waypoint.itinerary.constants.ItineraryConstants.USERNAME_URI_PARAM;

import com.waypoint.itinerary.domain.dto.ActivityDTO;
import com.waypoint.itinerary.domain.dto.PlaceDTO;
import com.waypoint.itinerary.domain.dto.TripDTO;
import com.waypoint.itinerary.domain.entity.*;
import com.waypoint.itinerary.exception.ErrorMessage;
import com.waypoint.itinerary.exception.GenericException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
public class ItineraryMapper {

  public static Mono<TripDTO> validateCreateTripRequest(TripDTO tripDTO) {
    log.info("validateCreateTripDetails :: TripDTO :: {}", tripDTO);
    if (StringUtils.isNotBlank(tripDTO.getTripName())
        && StringUtils.isNotBlank(tripDTO.getUserName())) {
      return Mono.just(tripDTO);
    }
    return Mono.error(new GenericException(ErrorMessage.MANDATORY_FIELDS_MISSING_OR_INVALID));
  }

  public static Mono<TripDTO> validateUpdateTripRequest(TripDTO tripDTO) {
    log.info("validateUpdateTripDetails :: TripDTO :: {}", tripDTO);
    if (tripDTO.getId() != null
        && (StringUtils.isNotBlank(tripDTO.getTripName())
            || StringUtils.isNotBlank(tripDTO.getDestination())
            || StringUtils.isNotBlank(tripDTO.getDescription())
            || tripDTO.getTripLength() != null
            || tripDTO.getIsPublic() != null
            || (tripDTO.getStartDate() != null && tripDTO.getEndDate() != null))) {
      return Mono.just(tripDTO);
    }
    return Mono.error(new GenericException(ErrorMessage.MANDATORY_FIELDS_MISSING_OR_INVALID));
  }

  public static Mono<Tuple2<String, UUID>> validateDeleteTripRequest(ServerRequest serverRequest) {
    log.info("validateDeleteTripRequest");
    String tripId = serverRequest.pathVariable("tripId");
    String userName = serverRequest.pathVariable(USERNAME_URI_PARAM);
    if (StringUtils.isNotBlank(tripId) && StringUtils.isNotBlank(userName)) {
      return Mono.just(Tuples.of(userName, UUID.fromString(tripId)));
    }
    return Mono.error(new GenericException(ErrorMessage.MANDATORY_FIELDS_MISSING_OR_INVALID));
  }

  public static Mono<UUID> validateDeleteActivityRequest(ServerRequest serverRequest) {
    log.info("validateDeleteActivityRequest");
    String activityId = serverRequest.pathVariable("activityId");
    if (StringUtils.isNotBlank(activityId)) {
      return Mono.just(UUID.fromString(activityId));
    }
    return Mono.error(new GenericException(ErrorMessage.MANDATORY_FIELDS_MISSING_OR_INVALID));
  }

  public static Mono<ActivityDTO> validateCreateActivityRequest(ActivityDTO activityDTO) {
    log.info("validateCreateActivityRequest :: ActivityDTO :: {}", activityDTO);
    if (activityDTO.getPlaceId() != null
        && activityDTO.getActivityTypeId() != null
        && activityDTO.getSortOrder() != null) {
      return Mono.just(activityDTO);
    }
    return Mono.error(new GenericException(ErrorMessage.MANDATORY_FIELDS_MISSING_OR_INVALID));
  }

  public static Mono<ActivityDTO> validateUpdateActivityRequest(ActivityDTO activityDTO) {
    log.info("validateUpdateActivityRequest :: ActivityDTO :: {}", activityDTO);
    if (activityDTO.getId() != null
        && (activityDTO.getActivityTypeId() != null
            || StringUtils.isNotBlank(activityDTO.getServiceProviderName())
            || activityDTO.getSortOrder() != null
            || (activityDTO.getStartTime() != null && activityDTO.getEndTime() != null)
            || StringUtils.isNotBlank(activityDTO.getDescription()))) {
      return Mono.just(activityDTO);
    }
    return Mono.error(new GenericException(ErrorMessage.MANDATORY_FIELDS_MISSING_OR_INVALID));
  }

  public static Trip getTrip(TripDTO tripDTO) {
    log.info("getTrip :: TripDTO :: {}", tripDTO);
    return Trip.builder()
        .tripName(tripDTO.getTripName())
        .destination(tripDTO.getDestination())
        .startDate(tripDTO.getStartDate())
        .endDate(tripDTO.getEndDate())
        .tripLength(tripDTO.getTripLength())
        .description(tripDTO.getDescription())
        .isPublic(tripDTO.getIsPublic())
        .isActive(true)
        .createdBy(DEF_USER_NAME)
        .createdOn(LocalDateTime.now())
        .updatedBy(DEF_USER_NAME)
        .updatedOn(LocalDateTime.now())
        .build();
  }

  public static TripDTO getTripDTO(Trip trip) {
    log.info("getTripDTO :: Trip :: {}", trip);
    return TripDTO.builder()
        .id(trip.getId())
        .tripId(trip.getTripId())
        .tripName(trip.getTripName())
        .destination(trip.getDestination())
        .startDate(trip.getStartDate())
        .endDate(trip.getEndDate())
        .tripLength(trip.getTripLength())
        .description(trip.getDescription())
        .isPublic(trip.getIsPublic())
        .isActive(trip.getIsActive())
        .createdBy(trip.getCreatedBy())
        .createdOn(trip.getCreatedOn())
        .updatedBy(trip.getUpdatedBy())
        .updatedOn(trip.getUpdatedOn())
        .build();
  }

  public static TripDTO getTripDTO(Trip trip, List<PlaceDTO> placeList) {
    log.info("getTripDTO :: Trip :: {}", trip);
    return getTripDTO(trip).withPlaceList(placeList);
  }

  public static Trip updateTrip(Trip trip, TripDTO tripDTO) {
    log.info("updateTrip :: Trip :: {}", trip);
    if (StringUtils.isNotBlank(tripDTO.getTripName())) {
      trip.setTripName(tripDTO.getTripName());
    }
    trip.setDestination(tripDTO.getDestination());
    trip.setTripLength(tripDTO.getTripLength());
    if (tripDTO.getStartDate() != null && tripDTO.getEndDate() != null) {
      if (!tripDTO.getStartDate().isAfter(tripDTO.getEndDate())) {
        trip.setTripLength(
            (int) ChronoUnit.DAYS.between(tripDTO.getStartDate(), tripDTO.getEndDate()) + 1);
        trip.setStartDate(tripDTO.getStartDate());
        trip.setEndDate(tripDTO.getEndDate());
      } else {
        trip.setTripLength(
            (int) ChronoUnit.DAYS.between(tripDTO.getEndDate(), tripDTO.getStartDate()) + 1);
        trip.setStartDate(tripDTO.getEndDate());
        trip.setEndDate(tripDTO.getStartDate());
      }
    } else {
      trip.setStartDate(null);
      trip.setEndDate(null);
    }
    trip.setDescription(tripDTO.getDescription());
    if (tripDTO.getIsPublic() != null) {
      trip.setIsPublic(tripDTO.getIsPublic());
    }
    trip.setUpdatedOn(LocalDateTime.now());
    return trip;
  }

  public static Activity updateActivity(Activity activity, ActivityDTO activityDTO) {
    log.info("updateActivity :: Activity :: {}", activity);
    if (activityDTO.getActivityTypeId() != null) {
      activity.setActivityTypeId(activityDTO.getActivityTypeId());
    }
    activity.setServiceProviderName(activityDTO.getServiceProviderName());
    if (activityDTO.getSortOrder() != null) {
      activity.setSortOrder(activityDTO.getSortOrder());
    }
    if (activityDTO.getStartTime() != null && activityDTO.getEndTime() != null) {
      if (!activityDTO.getStartTime().isAfter(activityDTO.getEndTime())) {
        activity.setStartTime(activityDTO.getStartTime());
        activity.setEndTime(activityDTO.getEndTime());
      } else {
        activity.setStartTime(activityDTO.getEndTime());
        activity.setEndTime(activityDTO.getStartTime());
      }
    } else {
      activity.setStartTime(null);
      activity.setEndTime(null);
    }
    activity.setDescription(activityDTO.getDescription());
    activity.setUpdatedOn(LocalDateTime.now());
    return activity;
  }

  public static PlaceActivityMap updatePlaceActivityMap(
      PlaceActivityMap placeActivityMap, UUID placeId, UUID activityId) {
    log.info("updatePlaceActivityMap :: PlaceActivityMap :: {}", placeActivityMap);
    placeActivityMap.setPlaceId(placeId);
    placeActivityMap.setActivityId(activityId);
    placeActivityMap.setUpdatedOn(LocalDateTime.now());
    return placeActivityMap;
  }

  public static Place getPlace(PlaceDTO placeDTO) {
    log.info("getPlace :: PlaceDTO :: {}", placeDTO);
    return Place.builder()
        .dayNo(placeDTO.getDayNo())
        .description(placeDTO.getDescription())
        .isActive(true)
        .createdBy(DEF_USER_NAME)
        .createdOn(LocalDateTime.now())
        .updatedBy(DEF_USER_NAME)
        .updatedOn(LocalDateTime.now())
        .build();
  }

  public static PlaceDTO getPlaceDTO(Place place) {
    log.info("getPlaceDTO :: Place :: {}", place);
    return PlaceDTO.builder()
        .id(place.getId())
        .placeId(place.getPlaceId())
        .dayNo(place.getDayNo())
        .description(place.getDescription())
        .isActive(place.getIsActive())
        .createdBy(place.getCreatedBy())
        .createdOn(place.getCreatedOn())
        .updatedBy(place.getUpdatedBy())
        .updatedOn(place.getUpdatedOn())
        .build();
  }

  public static PlaceDTO getPlaceDTO(Place place, List<ActivityDTO> activityList) {
    log.info("getPlaceDTO :: Place :: {}", place);
    return getPlaceDTO(place).withActivityList(activityList);
  }

  public static Activity getActivity(ActivityDTO activityDTO) {
    log.info("getActivity :: ActivityDTO :: {}", activityDTO);
    return Activity.builder()
        .activityTypeId(activityDTO.getActivityTypeId())
        .serviceProviderName(activityDTO.getServiceProviderName())
        .sortOrder(activityDTO.getSortOrder())
        .startTime(activityDTO.getStartTime())
        .endTime(activityDTO.getEndTime())
        .description(activityDTO.getDescription())
        .isActive(true)
        .createdBy(DEF_USER_NAME)
        .createdOn(LocalDateTime.now())
        .updatedBy(DEF_USER_NAME)
        .updatedOn(LocalDateTime.now())
        .build();
  }

  public static ActivityDTO getActivityDTO(Activity activity) {
    log.info("getActivityDTO :: Activity :: {}", activity);
    return ActivityDTO.builder()
        .id(activity.getId())
        .activityId(activity.getActivityId())
        .activityTypeId(activity.getActivityTypeId())
        .serviceProviderName(activity.getServiceProviderName())
        .sortOrder(activity.getSortOrder())
        .startTime(activity.getStartTime())
        .endTime(activity.getEndTime())
        .description(activity.getDescription())
        .isActive(activity.getIsActive())
        .createdBy(activity.getCreatedBy())
        .createdOn(activity.getCreatedOn())
        .updatedBy(activity.getUpdatedBy())
        .updatedOn(activity.getUpdatedOn())
        .build();
  }

  public static PlaceActivityMap getPlaceActivityMap(UUID placeId, UUID activityId) {
    log.info("getPlaceActivityMap :: placeId :: {} :: activityId :: {}", placeId, activityId);
    return PlaceActivityMap.builder()
        .placeId(placeId)
        .activityId(activityId)
        .isActive(true)
        .createdBy(DEF_USER_NAME)
        .createdOn(LocalDateTime.now())
        .updatedBy(DEF_USER_NAME)
        .updatedOn(LocalDateTime.now())
        .build();
  }

  public static Trip deleteTrip(Trip trip) {
    trip.setIsActive(false);
    trip.setUpdatedOn(LocalDateTime.now());
    return trip;
  }

  public static PlaceActivityMap deletePlaceActivityMap(PlaceActivityMap placeActivityMap) {
    placeActivityMap.setIsActive(false);
    placeActivityMap.setUpdatedOn(LocalDateTime.now());
    return placeActivityMap;
  }

  public static Activity deleteActivity(Activity activity) {
    activity.setIsActive(false);
    activity.setUpdatedOn(LocalDateTime.now());
    return activity;
  }

  public static TripPlaceMap deleteTripPlaceMap(TripPlaceMap tripPlaceMap) {
    tripPlaceMap.setIsActive(false);
    tripPlaceMap.setUpdatedOn(LocalDateTime.now());
    return tripPlaceMap;
  }

  public static Place deletePlace(Place place) {
    place.setIsActive(false);
    place.setUpdatedOn(LocalDateTime.now());
    return place;
  }
}
