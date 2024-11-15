package com.waypoint.itinerary.repository;

import com.waypoint.itinerary.domain.entity.PlaceMeta;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceMetaRepository extends ReactiveCrudRepository<PlaceMeta, UUID> {}
