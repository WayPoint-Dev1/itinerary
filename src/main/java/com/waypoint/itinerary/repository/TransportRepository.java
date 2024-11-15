package com.waypoint.itinerary.repository;

import com.waypoint.itinerary.domain.entity.Transport;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepository extends ReactiveCrudRepository<Transport, UUID> {}
