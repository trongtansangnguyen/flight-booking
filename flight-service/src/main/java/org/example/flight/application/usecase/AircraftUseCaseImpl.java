package org.example.flight.application.usecase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.flight.application.dto.AircraftRequest;
import org.example.flight.application.dto.AircraftResponse;
import org.example.flight.application.port.input.AircraftUseCase;
import org.example.flight.application.port.output.AircraftRepositoryPort;
import org.example.flight.domain.entity.Aircraft;
import org.example.flight.domain.exception.AircraftNotFoundException;

/**
 * Use Case Implementation
 * Application Layer - phụ thuộc vào Domain Layer
 * Không có dependency vào framework cụ thể (Spring, JPA, etc.)
 */
public class AircraftUseCaseImpl implements AircraftUseCase {
    
    private final AircraftRepositoryPort aircraftRepositoryPort;
    
    // Constructor injection - không dùng @Autowired hay @RequiredArgsConstructor
    public AircraftUseCaseImpl(AircraftRepositoryPort aircraftRepositoryPort) {
        this.aircraftRepositoryPort = aircraftRepositoryPort;
    }
    
    @Override
    public AircraftResponse createAircraft(AircraftRequest request) {
        // Business logic: Create domain entity
        Aircraft aircraft = Aircraft.create(
            request.getModel(),
            request.getName(),
            request.getCapacity()
        );
        
        // Save through port
        Aircraft savedAircraft = aircraftRepositoryPort.save(aircraft);
        
        // Map to response
        return mapToResponse(savedAircraft);
    }
    
    @Override
    public AircraftResponse updateAircraft(UUID id, AircraftRequest request) {
        // Find existing aircraft
        Aircraft aircraft = aircraftRepositoryPort.findById(id)
            .orElseThrow(() -> new AircraftNotFoundException("Aircraft not found with ID: " + id));
        
        // Business logic: Update entity
        aircraft.updateDetails(
            request.getModel(),
            request.getName(),
            request.getCapacity()
        );
        
        // Save through port
        Aircraft updatedAircraft = aircraftRepositoryPort.save(aircraft);
        
        return mapToResponse(updatedAircraft);
    }
    
    @Override
    public AircraftResponse getAircraftById(UUID id) {
        Aircraft aircraft = aircraftRepositoryPort.findById(id)
            .orElseThrow(() -> new AircraftNotFoundException("Aircraft not found with ID: " + id));
        
        return mapToResponse(aircraft);
    }
    
    @Override
    public List<AircraftResponse> getAllAircrafts() {
        return aircraftRepositoryPort.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<AircraftResponse> getAircraftsByModel(String model) {
        return aircraftRepositoryPort.findByModel(model).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<AircraftResponse> getAircraftsByMinCapacity(Integer minCapacity) {
        return aircraftRepositoryPort.findByMinCapacity(minCapacity).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<AircraftResponse> searchAircrafts(String keyword) {
        return aircraftRepositoryPort.searchAircrafts(keyword).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteAircraft(UUID id) {
        if (!aircraftRepositoryPort.existsById(id)) {
            throw new AircraftNotFoundException("Aircraft not found with ID: " + id);
        }
        
        aircraftRepositoryPort.deleteById(id);
    }
    
    // Mapper method
    private AircraftResponse mapToResponse(Aircraft aircraft) {
        return new AircraftResponse(
            aircraft.getId(),
            aircraft.getModel(),
            aircraft.getName(),
            aircraft.getCapacity(),
            aircraft.getCreatedAt(),
            aircraft.getUpdatedAt()
        );
    }
}
