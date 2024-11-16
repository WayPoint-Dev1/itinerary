package com.waypoint.itinerary.repository;

import com.waypoint.itinerary.domain.entity.TripPlaceMap;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TripPlaceMapRepository extends ReactiveCrudRepository<TripPlaceMap, UUID> {
  Flux<TripPlaceMap> findByTripIdAndIsActive(UUID tripId, Boolean isActive);
  Mono<TripPlaceMap> findByPlaceIdAndIsActive(UUID placeId, Boolean isActive);
}
