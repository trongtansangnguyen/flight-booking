package org.example.order.infrastructure.adapter.output.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.ports.output.FlightServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * REST Adapter for Flight Service
 * Infrastructure Layer - Output Adapter
 * Implements FlightServicePort from Application Layer
 */
@Slf4j
@Component
public class FlightServiceRestAdapter implements FlightServicePort {

    private final RestClient restClient;

    public FlightServiceRestAdapter(@Value("${flight.service.url:http://localhost:8081}") String flightServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(flightServiceUrl)
                .build();
    }

    @Override
    public Optional<LocalDateTime> getFlightDepartureTime(UUID flightId) {
        try {
            log.debug("Fetching flight departure time for flight: {}", flightId);
            
            FlightResponse response = restClient.get()
                    .uri("/api/flights/{flightId}", flightId)
                    .retrieve()
                    .body(FlightResponse.class);

            if (response != null && response.departureTime() != null) {
                log.debug("Flight {} departure time: {}", flightId, response.departureTime());
                return Optional.of(response.departureTime());
            }
            
            log.warn("Flight {} not found or missing departure time", flightId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error fetching flight departure time for flight: {}. Error: {}", 
                    flightId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * DTO for Flight Service response
     * Only includes fields needed for cancellation validation
     * Ignores other fields from FlightResponse
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record FlightResponse(
            @JsonProperty("id") UUID id,
            @JsonProperty("departureTime") LocalDateTime departureTime
    ) {
    }
}

