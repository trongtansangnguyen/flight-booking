package org.example.flight.infrastructure.adapter.output.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.flight.infrastructure.adapter.output.persistence.entity.AirportJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AirportJpaRepository extends JpaRepository<AirportJpaEntity, UUID> {

    Optional<AirportJpaEntity> findByCode(String code);

    @Query("SELECT a FROM AirportJpaEntity a WHERE " +
           "LOWER(a.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.country) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<AirportJpaEntity> searchAirports(@Param("keyword") String keyword);
}
