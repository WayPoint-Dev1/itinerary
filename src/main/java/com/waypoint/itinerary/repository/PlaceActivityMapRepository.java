package com.waypoint.itinerary.repository;

import com.waypoint.itinerary.domain.entity.PlaceActivityMap;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlaceActivityMapRepository
    extends ReactiveCrudRepository<PlaceActivityMap, UUID> {
    Flux<PlaceActivityMap> findByPlaceIdAndIsActive(UUID placeId, Boolean isActive);
    Mono<PlaceActivityMap> findByActivityIdAndIsActive(UUID activity, Boolean isActive);
    Mono<PlaceActivityMap> findByPlaceIdAndActivityIdAndIsActive(UUID placeId, UUID activityId, Boolean isActive);
}
