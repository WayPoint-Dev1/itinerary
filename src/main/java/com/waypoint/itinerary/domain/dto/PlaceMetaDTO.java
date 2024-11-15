package com.waypoint.itinerary.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlaceMetaDTO {
  private UUID id;
  private Integer placeMetaId;
  private String placeName;
  private UUID placeId;
  private UUID jurisdictionId;
  private Boolean isActive;
  private String createdBy;
  private LocalDateTime createdOn;
  private String updatedBy;
  private LocalDateTime updatedOn;
}
