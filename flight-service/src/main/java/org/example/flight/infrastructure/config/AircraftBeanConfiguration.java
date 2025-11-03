package org.example.flight.infrastructure.config;

import org.example.flight.application.port.input.AircraftUseCase;
import org.example.flight.application.port.output.AircraftRepositoryPort;
import org.example.flight.application.usecase.AircraftUseCaseImpl;
import org.example.flight.infrastructure.adapter.output.persistence.adapter.AircraftRepositoryAdapter;
import org.example.flight.infrastructure.adapter.output.persistence.repository.AircraftJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration - Infrastructure Layer (Container Layer)
 * Wiring tất cả các dependencies
 * 
 * DEPENDENCY RULE: Container phụ thuộc tất cả các layers
 * - Domain Layer (entities, exceptions)
 * - Application Layer (use cases, ports)
 * - Infrastructure Layer (adapters, repositories)
 * 
 * Đây là nơi DUY NHẤT biết về tất cả implementations
 */
@Configuration
public class AircraftBeanConfiguration {
    
    /**
     * Bean cho Repository Adapter (Output Adapter)
     * Implement AircraftRepositoryPort từ Application Layer
     */
    @Bean
    public AircraftRepositoryPort aircraftRepositoryPort(AircraftJpaRepository jpaRepository) {
        return new AircraftRepositoryAdapter(jpaRepository);
    }
    
    /**
     * Bean cho Use Case
     * Inject AircraftRepositoryPort (interface, không phải implementation cụ thể)
     */
    @Bean
    public AircraftUseCase aircraftUseCase(AircraftRepositoryPort aircraftRepositoryPort) {
        return new AircraftUseCaseImpl(aircraftRepositoryPort);
    }
}
