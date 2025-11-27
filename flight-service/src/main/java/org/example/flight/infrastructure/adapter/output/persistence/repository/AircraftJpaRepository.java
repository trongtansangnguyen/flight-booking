package org.example.flight.infrastructure.adapter.output.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.example.flight.infrastructure.adapter.output.persistence.entity.AircraftJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA Repository - Infrastructure Layer
 * Phụ thuộc vào Spring Data JPA
 */
public interface AircraftJpaRepository extends JpaRepository<AircraftJpaEntity, UUID> {
    
    List<AircraftJpaEntity> findByModel(String model);
    
    @Query("SELECT a FROM AircraftJpaEntity a WHERE a.capacity >= :minCapacity")
    List<AircraftJpaEntity> findByMinCapacity(@Param("minCapacity") Integer minCapacity);
    
    @Query("SELECT a FROM AircraftJpaEntity a WHERE " +
           "LOWER(a.model) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<AircraftJpaEntity> searchAircrafts(@Param("keyword") String keyword);
}
