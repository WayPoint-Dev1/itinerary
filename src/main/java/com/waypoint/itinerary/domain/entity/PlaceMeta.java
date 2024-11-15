package com.waypoint.itinerary.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "place_meta")
@Getter
@Setter
@Builder
public class PlaceMeta {
  @Id private UUID id;
  private Integer placeMetaId;
  private String placeName;
  private UUID placeId;
  private UUID jurisdictionId;
  private Boolean isActive;
  private String createdBy;
  private LocalDateTime createdOn;
  private String updatedBy;
  private LocalDateTime updatedOn;

  @Override
  public String toString() {
    return "PlaceMeta{"
        + "id="
        + id
        + ", placeMetaId="
        + placeMetaId
        + ", placeName='"
        + placeName
        + '\''
        + ", placeId="
        + placeId
        + ", jurisdictionId="
        + jurisdictionId
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
