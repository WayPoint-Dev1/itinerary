package com.waypoint.itinerary.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "transport")
@Getter
@Setter
@Builder
public class Transport {
  @Id private UUID id;
  private Integer transportId;
  private UUID startLocId;
  private UUID endLocId;
  private UUID transportTypeId;
  private Integer distance;
  private UUID modeOfTransport;
  private Boolean isActive;
  private String createdBy;
  private LocalDateTime createdOn;
  private String updatedBy;
  private LocalDateTime updatedOn;

  @Override
  public String toString() {
    return "Transport{"
        + "id="
        + id
        + ", transportId="
        + transportId
        + ", startLocId="
        + startLocId
        + ", endLocId="
        + endLocId
        + ", transportTypeId="
        + transportTypeId
        + ", distance="
        + distance
        + ", modeOfTransport="
        + modeOfTransport
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
