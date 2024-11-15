package com.waypoint.itinerary.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserTripMapDTO {
  private UUID id;
  private Integer userTripMapId;
  private UUID userId;
  private UUID tripId;
  private boolean isActive;
  private String createdBy;
  private LocalDateTime createdOn;
  private String updatedBy;
  private LocalDateTime updatedOn;
}
