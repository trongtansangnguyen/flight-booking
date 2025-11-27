package org.example.flight.infrastructure.config;

import org.example.flight.application.port.input.AirportUseCase;
import org.example.flight.application.port.output.AirportRepositoryPort;
import org.example.flight.application.usecase.AirportUseCaseImpl;
import org.example.flight.infrastructure.adapter.output.persistence.adapter.AirportRepositoryAdapter;
import org.example.flight.infrastructure.adapter.output.persistence.repository.AirportJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration - Infrastructure Layer (Container Layer) for Airport
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
public class AirportBeanConfiguration {
    
    /**
     * Bean cho Repository Adapter (Output Adapter)
     * Implement AirportRepositoryPort từ Application Layer
     */
    @Bean
    public AirportRepositoryPort airportRepositoryPort(AirportJpaRepository jpaRepository) {
        return new AirportRepositoryAdapter(jpaRepository);
    }
    
    /**
     * Bean cho Use Case
     * Inject AirportRepositoryPort (interface, không phải implementation cụ thể)
     */
    @Bean
    public AirportUseCase airportUseCase(AirportRepositoryPort airportRepositoryPort) {
        return new AirportUseCaseImpl(airportRepositoryPort);
    }
}

