package org.example.flight.application.port.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.flight.domain.entity.Aircraft;

/**
 * Output Port (Repository Interface)
 * Application Layer định nghĩa interface
 * Infrastructure Layer sẽ implement interface này
 * Dependency Inversion Principle: High-level module (Application) không phụ thuộc vào low-level module (Infrastructure)
 */
public interface AircraftRepositoryPort {
    
    Aircraft save(Aircraft aircraft);
    
    Optional<Aircraft> findById(UUID id);
    
    List<Aircraft> findAll();
    
    List<Aircraft> findByModel(String model);
    
    List<Aircraft> findByMinCapacity(Integer minCapacity);
    
    List<Aircraft> searchAircrafts(String keyword);
    
    boolean existsById(UUID id);
    
    void deleteById(UUID id);
}
