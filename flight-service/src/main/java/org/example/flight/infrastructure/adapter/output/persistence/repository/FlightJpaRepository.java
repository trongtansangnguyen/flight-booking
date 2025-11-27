package org.example.flight.infrastructure.adapter.output.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.flight.domain.entity.FlightStatus;
import org.example.flight.infrastructure.adapter.output.persistence.entity.FlightJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA Repository - Infrastructure Layer for Flight
 */
public interface FlightJpaRepository extends JpaRepository<FlightJpaEntity, UUID> {
    
    Optional<FlightJpaEntity> findByFlightNumber(String flightNumber);
    
    @Query("SELECT f FROM FlightJpaEntity f WHERE f.originAirport.id = :originAirportId")
    List<FlightJpaEntity> findByOriginAirportId(@Param("originAirportId") UUID originAirportId);
    
    @Query("SELECT f FROM FlightJpaEntity f WHERE f.destinationAirport.id = :destinationAirportId")
    List<FlightJpaEntity> findByDestinationAirportId(@Param("destinationAirportId") UUID destinationAirportId);
    
    @Query("SELECT f FROM FlightJpaEntity f WHERE f.aircraft.id = :aircraftId")
    List<FlightJpaEntity> findByAircraftId(@Param("aircraftId") UUID aircraftId);
    
    @Query("SELECT f FROM FlightJpaEntity f WHERE " +
           "(:originAirportId IS NULL OR f.originAirport.id = :originAirportId) AND " +
           "(:destinationAirportId IS NULL OR f.destinationAirport.id = :destinationAirportId) AND " +
           "(:startDate IS NULL OR f.departureTime >= :startDate) AND " +
           "(:endDate IS NULL OR f.departureTime <= :endDate)")
    List<FlightJpaEntity> searchFlights(@Param("originAirportId") UUID originAirportId,
                                        @Param("destinationAirportId") UUID destinationAirportId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT f FROM FlightJpaEntity f WHERE " +
           "f.departureTime >= :fromDate AND " +
           "f.status = :status AND " +
           "f.availableSeats > 0 " +
           "ORDER BY f.departureTime ASC")
    List<FlightJpaEntity> findUpcomingFlights(@Param("fromDate") LocalDateTime fromDate,
                                              @Param("status") FlightStatus status);
    
    List<FlightJpaEntity> findByStatus(FlightStatus status);
}

