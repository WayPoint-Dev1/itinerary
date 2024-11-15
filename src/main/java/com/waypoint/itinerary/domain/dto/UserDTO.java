package com.waypoint.itinerary.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserDTO {
  private UUID id;
  private String userName;
  private boolean available;
}
