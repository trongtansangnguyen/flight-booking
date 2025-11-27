package org.example.flight.application.port.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.flight.domain.entity.Airport;

/**
 * Output Port (Repository Interface) for Airport
 */
public interface AirportRepositoryPort {
    
    Airport save(Airport airport);
    
    Optional<Airport> findById(UUID id);
    
    Optional<Airport> findByCode(String code);
    
    List<Airport> findAll();
    
    List<Airport> searchAirports(String keyword);
    
    boolean existsById(UUID id);
    
    void deleteById(UUID id);
}
