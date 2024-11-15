package com.waypoint.itinerary.config;

import com.waypoint.itinerary.client.UserMappingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
  @Value("${user-mapping.base-endpoint}")
  private String userMappingEndPoint;

  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  public UserMappingClient getUserMappingClient() {
    return new UserMappingClient(webClientBuilder(), userMappingEndPoint);
  }
}
