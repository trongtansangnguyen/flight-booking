package org.example.flight.infrastructure.adapter.output.persistence.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.flight.application.port.output.FlightRepositoryPort;
import org.example.flight.domain.entity.Flight;
import org.example.flight.domain.entity.FlightStatus;
import org.example.flight.infrastructure.adapter.output.persistence.entity.AircraftJpaEntity;
import org.example.flight.infrastructure.adapter.output.persistence.entity.AirportJpaEntity;
import org.example.flight.infrastructure.adapter.output.persistence.entity.FlightJpaEntity;
import org.example.flight.infrastructure.adapter.output.persistence.repository.AircraftJpaRepository;
import org.example.flight.infrastructure.adapter.output.persistence.repository.AirportJpaRepository;
import org.example.flight.infrastructure.adapter.output.persistence.repository.FlightJpaRepository;

/**
 * Repository Adapter - Infrastructure Layer for Flight
 * Implements Output Port từ Application Layer
 */
public class FlightRepositoryAdapter implements FlightRepositoryPort {
    
    private final FlightJpaRepository jpaRepository;
    private final AirportJpaRepository airportJpaRepository;
    private final AircraftJpaRepository aircraftJpaRepository;
    
    public FlightRepositoryAdapter(FlightJpaRepository jpaRepository,
                                  AirportJpaRepository airportJpaRepository,
                                  AircraftJpaRepository aircraftJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.airportJpaRepository = airportJpaRepository;
        this.aircraftJpaRepository = aircraftJpaRepository;
    }
    
    @Override
    public Flight save(Flight flight) {
        FlightJpaEntity jpaEntity = toJpaEntity(flight);
        FlightJpaEntity saved = jpaRepository.save(jpaEntity);
        return toDomainEntity(saved);
    }
    
    @Override
    public Optional<Flight> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomainEntity);
    }
    
    @Override
    public Optional<Flight> findByFlightNumber(String flightNumber) {
        return jpaRepository.findByFlightNumber(flightNumber).map(this::toDomainEntity);
    }
    
    @Override
    public List<Flight> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Flight> findByOriginAirportId(UUID originAirportId) {
        return jpaRepository.findByOriginAirportId(originAirportId).stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Flight> findByDestinationAirportId(UUID destinationAirportId) {
        return jpaRepository.findByDestinationAirportId(destinationAirportId).stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Flight> findByAircraftId(UUID aircraftId) {
        return jpaRepository.findByAircraftId(aircraftId).stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Flight> searchFlights(UUID originAirportId, UUID destinationAirportId,
                                     LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.searchFlights(originAirportId, destinationAirportId, startDate, endDate).stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Flight> findUpcomingFlights(LocalDateTime fromDate) {
        return jpaRepository.findUpcomingFlights(fromDate, FlightStatus.SCHEDULED).stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    
    // Mapper: JPA Entity -> Domain Entity
    private Flight toDomainEntity(FlightJpaEntity jpa) {
        return new Flight(
            jpa.getId(),
            jpa.getFlightNumber(),
            jpa.getOriginAirport().getId(),
            jpa.getDestinationAirport().getId(),
            jpa.getDepartureTime(),
            jpa.getArrivalTime(),
            jpa.getAircraft().getId(),
            jpa.getBasePrice(),
            jpa.getAvailableSeats(),
            jpa.getStatus(),
            jpa.getCreatedAt(),
            jpa.getUpdatedAt()
        );
    }
    
    // Mapper: Domain Entity -> JPA Entity
    private FlightJpaEntity toJpaEntity(Flight flight) {
        // Load related entities
        AirportJpaEntity originAirport = airportJpaRepository.findById(flight.getOriginAirportId())
            .orElseThrow(() -> new RuntimeException("Origin airport not found: " + flight.getOriginAirportId()));
        
        AirportJpaEntity destinationAirport = airportJpaRepository.findById(flight.getDestinationAirportId())
            .orElseThrow(() -> new RuntimeException("Destination airport not found: " + flight.getDestinationAirportId()));
        
        AircraftJpaEntity aircraft = aircraftJpaRepository.findById(flight.getAircraftId())
            .orElseThrow(() -> new RuntimeException("Aircraft not found: " + flight.getAircraftId()));
        
        FlightJpaEntity jpa = new FlightJpaEntity();
        jpa.setId(flight.getId());
        jpa.setFlightNumber(flight.getFlightNumber());
        jpa.setOriginAirport(originAirport);
        jpa.setDestinationAirport(destinationAirport);
        jpa.setDepartureTime(flight.getDepartureTime());
        jpa.setArrivalTime(flight.getArrivalTime());
        jpa.setAircraft(aircraft);
        jpa.setBasePrice(flight.getBasePrice());
        jpa.setAvailableSeats(flight.getAvailableSeats());
        jpa.setStatus(flight.getStatus());
        jpa.setCreatedAt(flight.getCreatedAt());
        jpa.setUpdatedAt(flight.getUpdatedAt());
        // @PrePersist và @PreUpdate sẽ override nếu entity mới hoặc update
        
        return jpa;
    }
}

