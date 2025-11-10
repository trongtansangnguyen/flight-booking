package org.example.flight.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "flights")
public class FlightJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "flight_number", unique = true, nullable = false)
    private String flightNumber;

    @Column(name = "origin_airport_id", nullable = false)
    private UUID originAirportId;

    @Column(name = "destination_airport_id", nullable = false)
    private UUID destinationAirportId;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "aircraft_id", nullable = false)
    private UUID aircraftId;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public FlightJpaEntity() {
    }

    public FlightJpaEntity(UUID id, String flightNumber, UUID originAirportId, UUID destinationAirportId,
                         LocalDateTime departureTime, LocalDateTime arrivalTime, UUID aircraftId,
                         BigDecimal basePrice, Integer availableSeats, String status,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.originAirportId = originAirportId;
        this.destinationAirportId = destinationAirportId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraftId = aircraftId;
        this.basePrice = basePrice;
        this.availableSeats = availableSeats;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public UUID getOriginAirportId() {
        return originAirportId;
    }

    public UUID getDestinationAirportId() {
        return destinationAirportId;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public UUID getAircraftId() {
        return aircraftId;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}