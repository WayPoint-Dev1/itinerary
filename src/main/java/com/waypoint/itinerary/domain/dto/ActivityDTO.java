package com.waypoint.itinerary.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@Builder
@With
public class ActivityDTO {
  private UUID id;
  private Integer activityId;
  private UUID placeId;
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
}
