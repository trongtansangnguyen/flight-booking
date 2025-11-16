package org.example.flight.application.usecase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.flight.application.dto.FlightRequest;
import org.example.flight.application.dto.FlightResponse;
import org.example.flight.application.port.input.FlightUseCase;
import org.example.flight.application.port.output.AircraftRepositoryPort;
import org.example.flight.application.port.output.AirportRepositoryPort;
import org.example.flight.application.port.output.FlightRepositoryPort;
import org.example.flight.domain.entity.Aircraft;
import org.example.flight.domain.entity.Airport;
import org.example.flight.domain.entity.Flight;
import org.example.flight.domain.entity.FlightStatus;
import org.example.flight.domain.exception.AircraftNotFoundException;
import org.example.flight.domain.exception.AirportNotFoundException;
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
        // Validate airports
        Airport originAirport = airportRepositoryPort.findById(request.getOriginAirportId())
            .orElseThrow(() -> new AirportNotFoundException(
                "Origin airport not found with ID: " + request.getOriginAirportId()));
        
        Airport destinationAirport = airportRepositoryPort.findById(request.getDestinationAirportId())
            .orElseThrow(() -> new AirportNotFoundException(
                "Destination airport not found with ID: " + request.getDestinationAirportId()));
        
        // Validate aircraft
        Aircraft aircraft = aircraftRepositoryPort.findById(request.getAircraftId())
            .orElseThrow(() -> new AircraftNotFoundException(
                "Aircraft not found with ID: " + request.getAircraftId()));
        
        // Create flight with aircraft capacity as available seats
        Flight flight = Flight.create(
            request.getFlightNumber(),
            request.getOriginAirportId(),
            request.getDestinationAirportId(),
            request.getDepartureTime(),
            request.getArrivalTime(),
            request.getAircraftId(),
            request.getBasePrice(),
            aircraft.getCapacity()
        );
        
        Flight saved = flightRepositoryPort.save(flight);
        return mapToResponse(saved, originAirport, destinationAirport, aircraft);
    }
    
    @Override
    public FlightResponse updateFlight(UUID id, FlightRequest request) {
        Flight flight = flightRepositoryPort.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + id));
        
        // Validate airports
        Airport originAirport = airportRepositoryPort.findById(request.getOriginAirportId())
            .orElseThrow(() -> new AirportNotFoundException(
                "Origin airport not found with ID: " + request.getOriginAirportId()));
        
        Airport destinationAirport = airportRepositoryPort.findById(request.getDestinationAirportId())
            .orElseThrow(() -> new AirportNotFoundException(
                "Destination airport not found with ID: " + request.getDestinationAirportId()));
        
        // Validate aircraft
        Aircraft aircraft = aircraftRepositoryPort.findById(request.getAircraftId())
            .orElseThrow(() -> new AircraftNotFoundException(
                "Aircraft not found with ID: " + request.getAircraftId()));
        
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
        return mapToResponse(updated, originAirport, destinationAirport, aircraft);
    }
    
    @Override
    public FlightResponse getFlightById(UUID id) {
        Flight flight = flightRepositoryPort.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + id));
        
        Airport originAirport = airportRepositoryPort.findById(flight.getOriginAirportId())
            .orElseThrow(() -> new AirportNotFoundException(
                "Origin airport not found with ID: " + flight.getOriginAirportId()));
        
        Airport destinationAirport = airportRepositoryPort.findById(flight.getDestinationAirportId())
            .orElseThrow(() -> new AirportNotFoundException(
                "Destination airport not found with ID: " + flight.getDestinationAirportId()));
        
        Aircraft aircraft = aircraftRepositoryPort.findById(flight.getAircraftId())
            .orElseThrow(() -> new AircraftNotFoundException(
                "Aircraft not found with ID: " + flight.getAircraftId()));
        
        return mapToResponse(flight, originAirport, destinationAirport, aircraft);
    }
    
    @Override
    public FlightResponse getFlightByNumber(String flightNumber) {
        Flight flight = flightRepositoryPort.findByFlightNumber(flightNumber)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with number: " + flightNumber));
        
        Airport originAirport = airportRepositoryPort.findById(flight.getOriginAirportId())
            .orElseThrow(() -> new AirportNotFoundException(
                "Origin airport not found with ID: " + flight.getOriginAirportId()));
        
        Airport destinationAirport = airportRepositoryPort.findById(flight.getDestinationAirportId())
            .orElseThrow(() -> new AirportNotFoundException(
                "Destination airport not found with ID: " + flight.getDestinationAirportId()));
        
        Aircraft aircraft = aircraftRepositoryPort.findById(flight.getAircraftId())
            .orElseThrow(() -> new AircraftNotFoundException(
                "Aircraft not found with ID: " + flight.getAircraftId()));
        
        return mapToResponse(flight, originAirport, destinationAirport, aircraft);
    }
    
    @Override
    public List<FlightResponse> getAllFlights() {
        List<Flight> flights = flightRepositoryPort.findAll();
        return flights.stream()
            .map(flight -> {
                Airport originAirport = airportRepositoryPort.findById(flight.getOriginAirportId())
                    .orElse(null);
                Airport destinationAirport = airportRepositoryPort.findById(flight.getDestinationAirportId())
                    .orElse(null);
                Aircraft aircraft = aircraftRepositoryPort.findById(flight.getAircraftId())
                    .orElse(null);
                return mapToResponse(flight, originAirport, destinationAirport, aircraft);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public List<FlightResponse> searchFlights(UUID originAirportId, UUID destinationAirportId,
                                             LocalDateTime startDate, LocalDateTime endDate) {
        List<Flight> flights = flightRepositoryPort.searchFlights(
            originAirportId, destinationAirportId, startDate, endDate);
        
        return flights.stream()
            .map(flight -> {
                Airport originAirport = airportRepositoryPort.findById(flight.getOriginAirportId())
                    .orElse(null);
                Airport destinationAirport = airportRepositoryPort.findById(flight.getDestinationAirportId())
                    .orElse(null);
                Aircraft aircraft = aircraftRepositoryPort.findById(flight.getAircraftId())
                    .orElse(null);
                return mapToResponse(flight, originAirport, destinationAirport, aircraft);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public List<FlightResponse> getUpcomingFlights() {
        List<Flight> flights = flightRepositoryPort.findUpcomingFlights(LocalDateTime.now());
        return flights.stream()
            .map(flight -> {
                Airport originAirport = airportRepositoryPort.findById(flight.getOriginAirportId())
                    .orElse(null);
                Airport destinationAirport = airportRepositoryPort.findById(flight.getDestinationAirportId())
                    .orElse(null);
                Aircraft aircraft = aircraftRepositoryPort.findById(flight.getAircraftId())
                    .orElse(null);
                return mapToResponse(flight, originAirport, destinationAirport, aircraft);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public FlightResponse updateFlightStatus(UUID id, FlightStatus status) {
        Flight flight = flightRepositoryPort.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + id));
        
        flight.updateStatus(status);
        Flight updated = flightRepositoryPort.save(flight);
        
        Airport originAirport = airportRepositoryPort.findById(updated.getOriginAirportId())
            .orElse(null);
        Airport destinationAirport = airportRepositoryPort.findById(updated.getDestinationAirportId())
            .orElse(null);
        Aircraft aircraft = aircraftRepositoryPort.findById(updated.getAircraftId())
            .orElse(null);
        
        return mapToResponse(updated, originAirport, destinationAirport, aircraft);
    }
    
    @Override
    public void deleteFlight(UUID id) {
        if (!flightRepositoryPort.existsById(id)) {
            throw new FlightNotFoundException("Flight not found with ID: " + id);
        }
        flightRepositoryPort.deleteById(id);
    }
    
    private FlightResponse mapToResponse(Flight flight, Airport originAirport, 
                                        Airport destinationAirport, Aircraft aircraft) {
        return new FlightResponse(
            flight.getId(),
            flight.getFlightNumber(),
            flight.getOriginAirportId(),
            originAirport != null ? originAirport.getCode() : null,
            originAirport != null ? originAirport.getName() : null,
            originAirport != null ? originAirport.getCity() : null,
            flight.getDestinationAirportId(),
            destinationAirport != null ? destinationAirport.getCode() : null,
            destinationAirport != null ? destinationAirport.getName() : null,
            destinationAirport != null ? destinationAirport.getCity() : null,
            flight.getDepartureTime(),
            flight.getArrivalTime(),
            flight.getAircraftId(),
            aircraft != null ? aircraft.getModel() : null,
            aircraft != null ? aircraft.getName() : null,
            aircraft != null ? aircraft.getCapacity() : null,
            flight.getBasePrice(),
            flight.getAvailableSeats(),
            flight.getStatus(),
            flight.getCreatedAt(),
            flight.getUpdatedAt()
        );
    }
}

