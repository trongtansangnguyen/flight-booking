package org.example.flight.infrastructure.adapter.output.persistence.adapter;

import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.flight.application.port.output.FlightRepositoryPort;
import org.example.flight.domain.entity.Flight;
import org.example.flight.infrastructure.adapter.output.persistence.entity.FlightJpaEntity;
import org.example.flight.infrastructure.adapter.output.persistence.repository.FlightJpaRepository;

@Repository
public class FlightRepositoryAdapter implements FlightRepositoryPort {

    private final FlightJpaRepository jpaRepository;

    public FlightRepositoryAdapter(FlightJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
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
    public Optional<Flight> findByFlightNumber(String number) {
        return jpaRepository.findByFlightNumber(number).map(this::toDomainEntity);
    }

    @Override
    public List<Flight> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<Flight> searchFlights(String originCode, String destCode, LocalDateTime startDate, LocalDateTime endDate, String status) {
        return jpaRepository.searchFlights(originCode, destCode, startDate, endDate, status).stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<Flight> findUpcomingFlights(LocalDateTime now) {
        return jpaRepository.findUpcomingFlights(now).stream()
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

    private Flight toDomainEntity(FlightJpaEntity jpa) {
        return new Flight(
            jpa.getId(),
            jpa.getFlightNumber(),
            jpa.getOriginAirportId(),
            jpa.getDestinationAirportId(),
            jpa.getDepartureTime(),
            jpa.getArrivalTime(),
            jpa.getAircraftId(),
            jpa.getBasePrice(),
            jpa.getAvailableSeats(),
            jpa.getStatus(),
            jpa.getCreatedAt(),
            jpa.getUpdatedAt()
        );
    }

    private FlightJpaEntity toJpaEntity(Flight flight) {
        return new FlightJpaEntity(
            flight.getId(),
            flight.getFlightNumber(),
            flight.getOriginAirportId(),
            flight.getDestinationAirportId(),
            flight.getDepartureTime(),
            flight.getArrivalTime(),
            flight.getAircraftId(),
            flight.getBasePrice(),
            flight.getAvailableSeats(),
            flight.getStatus(),
            flight.getCreatedAt(),
            flight.getUpdatedAt()
        );
    }
}