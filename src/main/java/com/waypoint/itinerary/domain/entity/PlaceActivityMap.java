package com.waypoint.itinerary.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "place_activity_map")
@Getter
@Setter
@Builder
public class PlaceActivityMap {
  @Id private UUID id;
  private Integer placeActivityMapId;
  private UUID placeId;
  private UUID activityId;
  private Boolean isActive;
  private String createdBy;
  private LocalDateTime createdOn;
  private String updatedBy;
  private LocalDateTime updatedOn;

  @Override
  public String toString() {
    return "PlaceActivityMap{"
        + "id="
        + id
        + ", placeActivityMapId="
        + placeActivityMapId
        + ", placeId="
        + placeId
        + ", activityId="
        + activityId
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
