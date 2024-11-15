package com.waypoint.itinerary.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@With
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripDTO {
  private UUID id;
  private Integer tripId;
  private UUID userId;
  private String userName;
  private String tripName;
  private String destination;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer tripLength;
  private String description;
  private Boolean isPublic;
  private List<PlaceDTO> placeList;
  private Boolean isActive;
  private String createdBy;
  private LocalDateTime createdOn;
  private String updatedBy;
  private LocalDateTime updatedOn;
}
