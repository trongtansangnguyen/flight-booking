package org.example.flight.application.usecase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.flight.application.dto.FlightRequest;
import org.example.flight.application.dto.FlightResponse;
import org.example.flight.application.port.input.FlightUseCase;
import org.example.flight.application.port.output.FlightRepositoryPort;
import org.example.flight.application.port.output.AirportRepositoryPort;
import org.example.flight.application.port.output.AircraftRepositoryPort;
import org.example.flight.domain.entity.Flight;
import org.example.flight.domain.entity.Airport;
import org.example.flight.domain.entity.Aircraft;
import org.example.flight.domain.exception.FlightNotFoundException;

/**
 * Use Case Implementation for Flight
 */
public class FlightUseCaseImpl implements FlightUseCase {

    private final FlightRepositoryPort flightRepositoryPort;
    private final AirportRepositoryPort airportRepositoryPort;
    private final AircraftRepositoryPort aircraftRepositoryPort;

    public FlightUseCaseImpl(FlightRepositoryPort flightRepositoryPort,
                             AirportRepositoryPort airportRepositoryPort,
                             AircraftRepositoryPort aircraftRepositoryPort) {
        this.flightRepositoryPort = flightRepositoryPort;
        this.airportRepositoryPort = airportRepositoryPort;
        this.aircraftRepositoryPort = aircraftRepositoryPort;
    }

    @Override
    public FlightResponse createFlight(FlightRequest request) {
        // Domain creation - assumes Flight.create exists
        Flight flight = Flight.create(
            request.getFlightNumber(),
            request.getOriginAirportId(),
            request.getDestinationAirportId(),
            request.getDepartureTime(),
            request.getArrivalTime(),
            request.getAircraftId(),
            request.getBasePrice()
        );

        Flight saved = flightRepositoryPort.save(flight);
        return mapToResponse(saved);
    }

    @Override
    public FlightResponse updateFlight(UUID id, FlightRequest request) {
        Flight flight = flightRepositoryPort.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + id));

        flight.updateDetails(
            request.getFlightNumber(),
            request.getOriginAirportId(),
            request.getDestinationAirportId(),
            request.getDepartureTime(),
            request.getArrivalTime(),
            request.getAircraftId(),
            request.getBasePrice()
        );

        Flight updated = flightRepositoryPort.save(flight);
        return mapToResponse(updated);
    }

    @Override
    public FlightResponse getFlightById(UUID id) {
        Flight flight = flightRepositoryPort.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + id));
        return mapToResponse(flight);
    }

    @Override
    public FlightResponse getFlightByNumber(String number) {
        Flight flight = flightRepositoryPort.findByFlightNumber(number)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with number: " + number));
        return mapToResponse(flight);
    }

    @Override
    public List<FlightResponse> getAllFlights() {
        return flightRepositoryPort.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<FlightResponse> searchFlights(String originCode, String destCode, LocalDateTime startDate, LocalDateTime endDate, String status) {
        return flightRepositoryPort.searchFlights(originCode, destCode, startDate, endDate, status).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<FlightResponse> getUpcomingFlights() {
        return flightRepositoryPort.findUpcomingFlights(LocalDateTime.now()).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public FlightResponse updateFlightStatus(UUID id, String status) {
        Flight flight = flightRepositoryPort.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + id));

        flight.setStatus(status);
        Flight updated = flightRepositoryPort.save(flight);
        return mapToResponse(updated);
    }

    @Override
    public void deleteFlight(UUID id) {
        if (!flightRepositoryPort.existsById(id)) {
            throw new FlightNotFoundException("Flight not found with ID: " + id);
        }
        flightRepositoryPort.deleteById(id);
    }

    private FlightResponse mapToResponse(Flight flight) {
        // Enrich response with airport and aircraft details when available
        Airport origin = airportRepositoryPort.findById(flight.getOriginAirportId()).orElse(null);
        Airport dest = airportRepositoryPort.findById(flight.getDestinationAirportId()).orElse(null);
        Aircraft aircraft = aircraftRepositoryPort.findById(flight.getAircraftId()).orElse(null);

        return new FlightResponse(
            flight.getId(),
            flight.getFlightNumber(),
            flight.getOriginAirportId(),
            origin != null ? origin.getCode() : null,
            origin != null ? origin.getName() : null,
            origin != null ? origin.getCity() : null,
            flight.getDestinationAirportId(),
            dest != null ? dest.getCode() : null,
            dest != null ? dest.getName() : null,
            dest != null ? dest.getCity() : null,
            flight.getDepartureTime(),
            flight.getArrivalTime(),
            flight.getAircraftId(),
            aircraft != null ? aircraft.getModel() : null,
            aircraft != null ? aircraft.getName() : null,
            aircraft != null ? aircraft.getCapacity() : null,
            flight.getBasePrice(),
            flight.getAvailableSeats(),
            flight.getStatus()
        );
    }
}
