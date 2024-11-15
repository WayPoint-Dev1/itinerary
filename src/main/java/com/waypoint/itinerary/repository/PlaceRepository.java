package com.waypoint.itinerary.repository;

import com.waypoint.itinerary.domain.entity.Place;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlaceRepository extends ReactiveCrudRepository<Place, UUID> {
    Mono<Place> findByIdAndIsActive(UUID id, Boolean isActive);
}
