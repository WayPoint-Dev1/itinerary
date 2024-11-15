package com.waypoint.itinerary.repository;

import com.waypoint.itinerary.domain.entity.Activity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ActivityRepository extends ReactiveCrudRepository<Activity, UUID> {
    Mono<Activity> findByIdAndIsActive(UUID id, Boolean isActive);
}
