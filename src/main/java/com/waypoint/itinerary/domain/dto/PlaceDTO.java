package com.waypoint.itinerary.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@Builder
@With
public class PlaceDTO {
  private UUID id;
  private Integer placeId;
  private Integer dayNo;
  private String description;
  private PlaceMetaDTO placeMeta;
  private List<ActivityDTO> activityList;
  private Boolean isActive;
  private String createdBy;
  private LocalDateTime createdOn;
  private String updatedBy;
  private LocalDateTime updatedOn;
}
