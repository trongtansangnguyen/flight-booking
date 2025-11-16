package org.example.flight.infrastructure.config;

import org.example.flight.application.port.input.FlightUseCase;
import org.example.flight.application.port.output.AircraftRepositoryPort;
import org.example.flight.application.port.output.AirportRepositoryPort;
import org.example.flight.application.port.output.FlightRepositoryPort;
import org.example.flight.application.usecase.FlightUseCaseImpl;
import org.example.flight.infrastructure.adapter.output.persistence.adapter.FlightRepositoryAdapter;
import org.example.flight.infrastructure.adapter.output.persistence.repository.AircraftJpaRepository;
import org.example.flight.infrastructure.adapter.output.persistence.repository.AirportJpaRepository;
import org.example.flight.infrastructure.adapter.output.persistence.repository.FlightJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration - Infrastructure Layer (Container Layer) for Flight
 * Wiring tất cả các dependencies
 */
@Configuration
public class FlightBeanConfiguration {
    
    /**
     * Bean cho Repository Adapter (Output Adapter)
     * Implement FlightRepositoryPort từ Application Layer
     */
    @Bean
    public FlightRepositoryPort flightRepositoryPort(FlightJpaRepository flightJpaRepository,
                                                     AirportJpaRepository airportJpaRepository,
                                                     AircraftJpaRepository aircraftJpaRepository) {
        return new FlightRepositoryAdapter(flightJpaRepository, airportJpaRepository, aircraftJpaRepository);
    }
    
    /**
     * Bean cho Use Case
     * Inject FlightRepositoryPort, AirportRepositoryPort, và AircraftRepositoryPort
     */
    @Bean
    public FlightUseCase flightUseCase(FlightRepositoryPort flightRepositoryPort,
                                       AirportRepositoryPort airportRepositoryPort,
                                       AircraftRepositoryPort aircraftRepositoryPort) {
        return new FlightUseCaseImpl(flightRepositoryPort, airportRepositoryPort, aircraftRepositoryPort);
    }
}

