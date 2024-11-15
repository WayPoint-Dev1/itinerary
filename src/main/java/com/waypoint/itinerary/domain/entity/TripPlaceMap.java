package com.waypoint.itinerary.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "trip_place_map")
@Getter
@Setter
@Builder
public class TripPlaceMap {
  @Id private UUID id;
  private Integer tripPlaceMapId;
  private UUID tripId;
  private UUID placeId;
  private Boolean isActive;
  private String createdBy;
  private LocalDateTime createdOn;
  private String updatedBy;
  private LocalDateTime updatedOn;

  @Override
  public String toString() {
    return "TripPlaceMap{"
        + "id="
        + id
        + ", tripPlaceMapId="
        + tripPlaceMapId
        + ", tripId="
        + tripId
        + ", placeId="
        + placeId
        + ", isActive="
        + isActive
        + ", createdBy='"
        + createdBy
        + '\''
        + ", createdOn="
        + createdOn
        + ", updatedBy='"
        + updatedBy
        + '\''
        + ", updatedOn="
        + updatedOn
        + '}';
  }
}
