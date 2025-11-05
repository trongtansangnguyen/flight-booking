package org.example.flight.application.usecase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.flight.application.dto.AirportRequest;
import org.example.flight.application.dto.AirportResponse;
import org.example.flight.application.port.input.AirportUseCase;
import org.example.flight.application.port.output.AirportRepositoryPort;
import org.example.flight.domain.entity.Airport;
import org.example.flight.domain.exception.AirportNotFoundException;

/**
 * Use Case Implementation for Airport
 */
public class AirportUseCaseImpl implements AirportUseCase {
    
    private final AirportRepositoryPort airportRepositoryPort;
    
    public AirportUseCaseImpl(AirportRepositoryPort airportRepositoryPort) {
        this.airportRepositoryPort = airportRepositoryPort;
    }
    
    @Override
    public AirportResponse createAirport(AirportRequest request) {
        Airport airport = Airport.create(
            request.getCode(),
            request.getName(),
            request.getCity(),
            request.getCountry()
        );
        Airport saved = airportRepositoryPort.save(airport);
        return mapToResponse(saved);
    }

    @Override
    public AirportResponse updateAirport(UUID id, AirportRequest request) {
        Airport airport = airportRepositoryPort.findById(id)
            .orElseThrow(() -> new AirportNotFoundException("Airport not found with ID: " + id));

        airport.updateDetails(
            request.getCode(),
            request.getName(),
            request.getCity(),
            request.getCountry()
        );

        Airport updated = airportRepositoryPort.save(airport);
        return mapToResponse(updated);
    }

    @Override
    public AirportResponse getAirportById(UUID id) {
        Airport airport = airportRepositoryPort.findById(id)
            .orElseThrow(() -> new AirportNotFoundException("Airport not found with ID: " + id));
        return mapToResponse(airport);
    }

    @Override
    public AirportResponse getAirportByCode(String code) {
        Airport airport = airportRepositoryPort.findByCode(code)
            .orElseThrow(() -> new AirportNotFoundException("Airport not found with code: " + code));
        return mapToResponse(airport);
    }

    @Override
    public List<AirportResponse> getAllAirports() {
        return airportRepositoryPort.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<AirportResponse> searchAirports(String keyword) {
        return airportRepositoryPort.searchAirports(keyword).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteAirport(UUID id) {
        if (!airportRepositoryPort.existsById(id)) {
            throw new AirportNotFoundException("Airport not found with ID: " + id);
        }
        airportRepositoryPort.deleteById(id);
    }

    private AirportResponse mapToResponse(Airport airport) {
        return new AirportResponse(
            airport.getId(),
            airport.getCode(),
            airport.getName(),
            airport.getCity(),
            airport.getCountry(),
            airport.getCreatedAt(),
            airport.getUpdatedAt()
        );
    }
}
