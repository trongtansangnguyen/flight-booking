package org.example.flight.application.port.input;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.example.flight.application.dto.FlightRequest;
import org.example.flight.application.dto.FlightResponse;

/**
 * Input Port (Use Case Interface) for Flight
 */
public interface FlightUseCase {

    FlightResponse createFlight(FlightRequest request);

    FlightResponse updateFlight(UUID id, FlightRequest request);

    FlightResponse getFlightById(UUID id);

    FlightResponse getFlightByNumber(String number);

    List<FlightResponse> getAllFlights();

    /**
     * Search flights by optional filters. Any null parameter will be ignored by implementation.
     */
    List<FlightResponse> searchFlights(String originCode, String destCode,
                                       LocalDateTime startDate, LocalDateTime endDate,
                                       String status);

    List<FlightResponse> getUpcomingFlights();

    FlightResponse updateFlightStatus(UUID id, String status);

    void deleteFlight(UUID id);
}
