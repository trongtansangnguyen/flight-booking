package org.example.flight.application.port.output;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.flight.domain.entity.Flight;

/**
 * Output Port (Repository Interface) for Flight
 */
public interface FlightRepositoryPort {

    Flight save(Flight flight);

    Optional<Flight> findById(UUID id);

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findAll();

    List<Flight> searchFlights(String originCode, String destCode, LocalDateTime startDate, LocalDateTime endDate, String status);

    List<Flight> findUpcomingFlights(LocalDateTime now);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
