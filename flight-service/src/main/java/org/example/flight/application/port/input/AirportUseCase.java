package org.example.flight.application.port.input;

import java.util.List;
import java.util.UUID;

import org.example.flight.application.dto.AirportRequest;
import org.example.flight.application.dto.AirportResponse;

/**
 * Input Port (Use Case Interface) for Airport
 */
public interface AirportUseCase {
    
    AirportResponse createAirport(AirportRequest request);
    
    AirportResponse updateAirport(UUID id, AirportRequest request);
    
    AirportResponse getAirportById(UUID id);
    
    AirportResponse getAirportByCode(String code);
    
    List<AirportResponse> getAllAirports();
    
    List<AirportResponse> searchAirports(String keyword);
    
    void deleteAirport(UUID id);
}
