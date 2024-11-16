package com.waypoint.itinerary.repository;

import com.waypoint.itinerary.domain.entity.PlaceMeta;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PlaceMetaRepository extends ReactiveCrudRepository<PlaceMeta, UUID> {
    Mono<PlaceMeta> findByPlaceIdAndIsActive(UUID placeId, Boolean isActive);
}
