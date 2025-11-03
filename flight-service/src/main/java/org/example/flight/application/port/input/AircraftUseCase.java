package org.example.flight.application.port.input;

import java.util.List;
import java.util.UUID;

import org.example.flight.application.dto.AircraftRequest;
import org.example.flight.application.dto.AircraftResponse;

/**
 * Input Port (Use Case Interface)
 * Application Layer - phụ thuộc vào Domain Layer
 * Định nghĩa các use case mà application hỗ trợ
 */
public interface AircraftUseCase {
    
    AircraftResponse createAircraft(AircraftRequest request);
    
    AircraftResponse updateAircraft(UUID id, AircraftRequest request);
    
    AircraftResponse getAircraftById(UUID id);
    
    List<AircraftResponse> getAllAircrafts();
    
    List<AircraftResponse> getAircraftsByModel(String model);
    
    List<AircraftResponse> getAircraftsByMinCapacity(Integer minCapacity);
    
    List<AircraftResponse> searchAircrafts(String keyword);
    
    void deleteAircraft(UUID id);
}
