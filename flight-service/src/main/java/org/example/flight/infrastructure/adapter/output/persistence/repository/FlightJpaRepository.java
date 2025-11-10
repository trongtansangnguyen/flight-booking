package org.example.flight.infrastructure.adapter.output.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.flight.infrastructure.adapter.output.persistence.entity.FlightJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FlightJpaRepository extends JpaRepository<FlightJpaEntity, UUID> {

    Optional<FlightJpaEntity> findByFlightNumber(String flightNumber);

    @Query("SELECT f FROM FlightJpaEntity f " +
           "WHERE (:originCode IS NULL OR f.originAirportId IN " +
           "    (SELECT a.id FROM AirportJpaEntity a WHERE a.code = :originCode)) " +
           "AND (:destCode IS NULL OR f.destinationAirportId IN " +
           "    (SELECT a.id FROM AirportJpaEntity a WHERE a.code = :destCode)) " +
           "AND (:startDate IS NULL OR f.departureTime >= :startDate) " +
           "AND (:endDate IS NULL OR f.departureTime <= :endDate) " +
           "AND (:status IS NULL OR f.status = :status)")
    List<FlightJpaEntity> searchFlights(
        @Param("originCode") String originCode,
        @Param("destCode") String destCode,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("status") String status
    );

    @Query("SELECT f FROM FlightJpaEntity f " +
           "WHERE f.departureTime > :now " +
           "AND f.status = 'SCHEDULED' " +
           "ORDER BY f.departureTime ASC")
    List<FlightJpaEntity> findUpcomingFlights(@Param("now") LocalDateTime now);
}