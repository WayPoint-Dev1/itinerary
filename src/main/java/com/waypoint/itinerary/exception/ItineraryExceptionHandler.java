package com.waypoint.itinerary.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ItineraryExceptionHandler {

  @ExceptionHandler(GenericException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleGenericException(GenericException ex) {
    return Mono.just(
        ResponseEntity.status(ex.getErrorMessage().getHttpStatusCode())
            .body(
                ErrorResponse.builder()
                    .timestamp(LocalDateTime.now().toString())
                    .status(ex.getErrorMessage().getHttpStatusCode())
                    .error(ex.getErrorMessage().getMessageCode())
                    .message(ex.getErrorMessage().getMessageDescription())
                    .build()));
  }

  @ExceptionHandler(ExternalClientException.class)
  public Mono<ResponseEntity<ErrorResponse>> handlExternalClientException(
      ExternalClientException ex) {
    return Mono.just(
        ResponseEntity.status(ex.getHttpStatusCode())
            .body(
                ErrorResponse.builder()
                    .timestamp(LocalDateTime.now().toString())
                    .status(ex.getHttpStatusCode())
                    .error(ex.getMessageCode())
                    .message(ex.getMessageDescription())
                    .build()));
  }
}
