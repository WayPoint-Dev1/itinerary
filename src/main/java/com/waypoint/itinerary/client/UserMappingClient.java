package com.waypoint.itinerary.client;

import static com.waypoint.itinerary.constants.ItineraryConstants.CREATE_USER_TRIP_MAPPING_URI;
import static com.waypoint.itinerary.constants.ItineraryConstants.VALIDATE_USERNAME_URI;

import com.waypoint.itinerary.domain.dto.UserDTO;
import com.waypoint.itinerary.domain.dto.UserTripMapDTO;
import com.waypoint.itinerary.exception.ErrorResponse;
import com.waypoint.itinerary.exception.ExternalClientException;
import java.net.ConnectException;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
public class UserMappingClient {
  public WebClient.Builder webClientBuilder;
  public String baseEndPoint;

  public UserMappingClient(WebClient.Builder webClientBuilder, final String baseEndpoint) {
    this.webClientBuilder = webClientBuilder;
    this.baseEndPoint = baseEndpoint;
  }

  public Mono<UserTripMapDTO> createUserTripMapping(String username, UUID tripId) {
    log.info("createUserTripMapping :: username :: {} :: tripId :: {}", username, tripId);
    return Mono.defer(
            () ->
                webClientBuilder
                    .build()
                    .post()
                    .uri(
                        baseEndPoint.concat(
                            CREATE_USER_TRIP_MAPPING_URI
                                .replace("{username}", username)
                                .replace("{tripId}", tripId.toString())))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(
                        HttpStatusCode::isError,
                        clientResponse ->
                            clientResponse
                                .bodyToMono(ErrorResponse.class)
                                .flatMap(
                                    errorResponse ->
                                        Mono.error(
                                            new ExternalClientException(
                                                errorResponse.getStatus(),
                                                errorResponse.getError(),
                                                errorResponse.getMessage()))))
                    .bodyToMono(UserTripMapDTO.class))
        .retryWhen(
            Retry.backoff(3, Duration.ofSeconds(10))
                .filter(
                    throwable ->
                        !(throwable instanceof ExternalClientException)
                            && throwable.getCause().getCause() instanceof ConnectException));
  }

  public Mono<UserDTO> validateUserName(String username) {
    log.info("validateUserName :: username :: {}", username);
    return Mono.defer(
            () ->
                webClientBuilder
                    .build()
                    .get()
                    .uri(baseEndPoint.concat(VALIDATE_USERNAME_URI.replace("{username}", username)))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(
                        HttpStatusCode::isError,
                        clientResponse ->
                            clientResponse
                                .bodyToMono(ErrorResponse.class)
                                .flatMap(
                                    errorResponse ->
                                        Mono.error(
                                            new ExternalClientException(
                                                errorResponse.getStatus(),
                                                errorResponse.getError(),
                                                errorResponse.getMessage()))))
                    .bodyToMono(UserDTO.class))
        .retryWhen(
            Retry.backoff(3, Duration.ofSeconds(10))
                .filter(
                    throwable ->
                        !(throwable instanceof ExternalClientException)
                            && throwable.getCause().getCause() instanceof ConnectException));
  }

  public Mono<UserDTO> deleteUserTripMapping(String username, UUID tripId) {
    log.info("deleteUserTripMapping :: username :: {} :: tripId :: {}", username, tripId);
    return Mono.defer(
            () ->
                webClientBuilder
                    .build()
                    .delete()
                    .uri(
                        baseEndPoint.concat(
                            CREATE_USER_TRIP_MAPPING_URI
                                .replace("{username}", username)
                                .replace("{tripId}", tripId.toString())))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(
                        HttpStatusCode::isError,
                        clientResponse ->
                            clientResponse
                                .bodyToMono(ErrorResponse.class)
                                .flatMap(
                                    errorResponse ->
                                        Mono.error(
                                            new ExternalClientException(
                                                errorResponse.getStatus(),
                                                errorResponse.getError(),
                                                errorResponse.getMessage()))))
                    .bodyToMono(UserDTO.class))
        .retryWhen(
            Retry.backoff(3, Duration.ofSeconds(10))
                .filter(
                    throwable ->
                        !(throwable instanceof ExternalClientException)
                            && throwable.getCause().getCause() instanceof ConnectException));
  }
}
