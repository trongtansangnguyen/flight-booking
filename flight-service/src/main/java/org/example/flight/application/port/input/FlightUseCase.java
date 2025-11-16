package org.example.flight.application.port.input;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.example.flight.application.dto.FlightRequest;
import org.example.flight.application.dto.FlightResponse;
import org.example.flight.domain.entity.FlightStatus;

/**
 * Input Port (Use Case Interface) for Flight
 */
public interface FlightUseCase {
    
    FlightResponse createFlight(FlightRequest request);
    
    FlightResponse updateFlight(UUID id, FlightRequest request);
    
    FlightResponse getFlightById(UUID id);
    
    FlightResponse getFlightByNumber(String flightNumber);
    
    List<FlightResponse> getAllFlights();
    
    List<FlightResponse> searchFlights(UUID originAirportId, UUID destinationAirportId,
                                      LocalDateTime startDate, LocalDateTime endDate);
    
    List<FlightResponse> getUpcomingFlights();
    
    FlightResponse updateFlightStatus(UUID id, FlightStatus status);
    
    void deleteFlight(UUID id);
}

