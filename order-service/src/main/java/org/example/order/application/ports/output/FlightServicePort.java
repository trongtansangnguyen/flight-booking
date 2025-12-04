package org.example.order.application.ports.output;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Output Port for getting flight information from Flight Service
 * Application Layer defines the interface
 */
public interface FlightServicePort {
    
    /**
     * Get flight departure time by flight ID
     * @param flightId Flight ID
     * @return Optional departure time, empty if flight not found
     */
    Optional<LocalDateTime> getFlightDepartureTime(UUID flightId);
}

