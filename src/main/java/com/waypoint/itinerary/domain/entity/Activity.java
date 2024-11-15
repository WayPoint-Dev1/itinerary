package com.waypoint.itinerary.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "activity")
@Getter
@Setter
@Builder
public class Activity {
  @Id private UUID id;
  private Integer activityId;
  private UUID activityTypeId;
  private String serviceProviderName;
  private Integer sortOrder;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String description;
  private Boolean isActive;
  private String createdBy;
  private LocalDateTime createdOn;
  private String updatedBy;
  private LocalDateTime updatedOn;

  @Override
  public String toString() {
    return "Activity{"
        + "id="
        + id
        + ", activityId="
        + activityId
        + ", activityTypeId="
        + activityTypeId
        + ", serviceProviderName='"
        + serviceProviderName
        + '\''
        + ", sortOrder='"
        + sortOrder
        + '\''
        + ", startTime="
        + startTime
        + ", endTime="
        + endTime
        + ", description='"
        + description
        + '\''
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
