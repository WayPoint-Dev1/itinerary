package com.waypoint.itinerary.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "trip")
@Getter
@Setter
@Builder
public class Trip {
  @Id private UUID id;
  private Integer tripId;
  private String tripName;
  private String destination;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer tripLength;
  private String description;
  private Boolean isPublic;
  private Boolean isActive;
  private String createdBy;
  private LocalDateTime createdOn;
  private String updatedBy;
  private LocalDateTime updatedOn;

  @Override
  public String toString() {
    return "Trip{"
        + "id="
        + id
        + ", tripId="
        + tripId
        + ", tripName='"
        + tripName
        + '\''
        + ", destination='"
        + destination
        + '\''
        + ", startDate="
        + startDate
        + ", endDate="
        + endDate
        + ", tripLength='"
        + tripLength
        + '\''
        + ", description='"
        + description
        + '\''
        + ", isPublic="
        + isPublic
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
