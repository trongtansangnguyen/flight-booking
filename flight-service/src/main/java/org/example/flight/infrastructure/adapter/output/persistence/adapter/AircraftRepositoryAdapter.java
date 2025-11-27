package org.example.flight.infrastructure.adapter.output.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.flight.application.port.output.AircraftRepositoryPort;
import org.example.flight.domain.entity.Aircraft;
import org.example.flight.infrastructure.adapter.output.persistence.entity.AircraftJpaEntity;
import org.example.flight.infrastructure.adapter.output.persistence.repository.AircraftJpaRepository;

/**
 * Repository Adapter - Infrastructure Layer
 * Implements Output Port từ Application Layer
 * Phụ thuộc vào: Domain, Application, và JPA Framework
 * 
 * DEPENDENCY RULE: Infrastructure phụ thuộc Application và Domain
 */
public class AircraftRepositoryAdapter implements AircraftRepositoryPort {
    
    private final AircraftJpaRepository jpaRepository;
    
    public AircraftRepositoryAdapter(AircraftJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Aircraft save(Aircraft aircraft) {
        AircraftJpaEntity jpaEntity = toJpaEntity(aircraft);
        AircraftJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return toDomainEntity(savedEntity);
    }
    
    @Override
    public Optional<Aircraft> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(this::toDomainEntity);
    }
    
    @Override
    public List<Aircraft> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Aircraft> findByModel(String model) {
        return jpaRepository.findByModel(model).stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Aircraft> findByMinCapacity(Integer minCapacity) {
        return jpaRepository.findByMinCapacity(minCapacity).stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Aircraft> searchAircrafts(String keyword) {
        return jpaRepository.searchAircrafts(keyword).stream()
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
    private Aircraft toDomainEntity(AircraftJpaEntity jpaEntity) {
        return new Aircraft(
            jpaEntity.getId(),
            jpaEntity.getModel(),
            jpaEntity.getName(),
            jpaEntity.getCapacity(),
            jpaEntity.getCreatedAt(),
            jpaEntity.getUpdatedAt()
        );
    }
    
    // Mapper: Domain Entity -> JPA Entity
    private AircraftJpaEntity toJpaEntity(Aircraft aircraft) {
        return new AircraftJpaEntity(
            aircraft.getId(),
            aircraft.getModel(),
            aircraft.getName(),
            aircraft.getCapacity(),
            aircraft.getCreatedAt(),
            aircraft.getUpdatedAt()
        );
    }
}
