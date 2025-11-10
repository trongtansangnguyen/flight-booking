package org.example.flight.infrastructure.adapter.output.persistence.adapter;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.flight.application.port.output.AirportRepositoryPort;
import org.example.flight.domain.entity.Airport;
import org.example.flight.infrastructure.adapter.output.persistence.entity.AirportJpaEntity;
import org.example.flight.infrastructure.adapter.output.persistence.repository.AirportJpaRepository;

@Repository
public class AirportRepositoryAdapter implements AirportRepositoryPort {

    private final AirportJpaRepository jpaRepository;

    public AirportRepositoryAdapter(AirportJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Airport save(Airport airport) {
        AirportJpaEntity jpaEntity = toJpaEntity(airport);
        AirportJpaEntity saved = jpaRepository.save(jpaEntity);
        return toDomainEntity(saved);
    }

    @Override
    public Optional<Airport> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomainEntity);
    }

    @Override
    public Optional<Airport> findByCode(String code) {
        return jpaRepository.findByCode(code).map(this::toDomainEntity);
    }

    @Override
    public List<Airport> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomainEntity).collect(Collectors.toList());
    }

    @Override
    public List<Airport> searchAirports(String keyword) {
        return jpaRepository.searchAirports(keyword).stream().map(this::toDomainEntity).collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    private Airport toDomainEntity(AirportJpaEntity jpa) {
        return new Airport(
            jpa.getId(),
            jpa.getCode(),
            jpa.getName(),
            jpa.getCity(),
            jpa.getCountry(),
            jpa.getCreatedAt(),
            jpa.getUpdatedAt()
        );
    }

    private AirportJpaEntity toJpaEntity(Airport airport) {
        return new AirportJpaEntity(
            airport.getId(),
            airport.getCode(),
            airport.getName(),
            airport.getCity(),
            airport.getCountry(),
            airport.getCreatedAt(),
            airport.getUpdatedAt()
        );
    }
}
