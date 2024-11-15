package com.waypoint.itinerary.repository;

import com.waypoint.itinerary.domain.entity.Trip;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TripRepository extends ReactiveCrudRepository<Trip, UUID> {
    Mono<Trip> findByIdAndIsActive(UUID id, Boolean isActive);
}
