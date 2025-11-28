package org.example.flight.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Entity - Flight
 * Pure business logic, no framework dependencies
 */
public class Flight {
    
    private UUID id;
    private String flightNumber;
    private UUID originAirportId;
    private UUID destinationAirportId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private UUID aircraftId;
    private BigDecimal basePrice;
    private Integer availableSeats;
    private FlightStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor
    public Flight() {
    }
    
    public Flight(UUID id, String flightNumber, UUID originAirportId, UUID destinationAirportId,
                  LocalDateTime departureTime, LocalDateTime arrivalTime, UUID aircraftId,
                  BigDecimal basePrice, Integer availableSeats, FlightStatus status,
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
    
    // Factory method for creating new flight
    public static Flight create(String flightNumber, UUID originAirportId, UUID destinationAirportId,
                                LocalDateTime departureTime, LocalDateTime arrivalTime, UUID aircraftId,
                                BigDecimal basePrice, Integer aircraftCapacity) {
        Flight flight = new Flight();
        flight.flightNumber = flightNumber;
        flight.originAirportId = originAirportId;
        flight.destinationAirportId = destinationAirportId;
        flight.departureTime = departureTime;
        flight.arrivalTime = arrivalTime;
        flight.aircraftId = aircraftId;
        flight.basePrice = basePrice;
        flight.availableSeats = aircraftCapacity; // Set available seats = aircraft capacity
        flight.status = FlightStatus.SCHEDULED;
        flight.createdAt = LocalDateTime.now();
        flight.updatedAt = LocalDateTime.now();
        return flight;
    }
    
    // Business logic methods
    public void updateDetails(String flightNumber, UUID originAirportId, UUID destinationAirportId,
                             LocalDateTime departureTime, LocalDateTime arrivalTime, UUID aircraftId,
                             BigDecimal basePrice) {
        this.flightNumber = flightNumber;
        this.originAirportId = originAirportId;
        this.destinationAirportId = destinationAirportId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraftId = aircraftId;
        this.basePrice = basePrice;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateStatus(FlightStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void reserveSeat(int count) {
        if (this.availableSeats >= count) {
            this.availableSeats -= count;
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalStateException("sold_out");
        }
    }
    
    public void releaseSeat(int count) {
        this.availableSeats += count;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasAvailableSeats() {
        return this.availableSeats > 0;
    }
    
    public boolean isScheduled() {
        return this.status == FlightStatus.SCHEDULED;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getFlightNumber() {
        return flightNumber;
    }
    
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    public UUID getOriginAirportId() {
        return originAirportId;
    }
    
    public void setOriginAirportId(UUID originAirportId) {
        this.originAirportId = originAirportId;
    }
    
    public UUID getDestinationAirportId() {
        return destinationAirportId;
    }
    
    public void setDestinationAirportId(UUID destinationAirportId) {
        this.destinationAirportId = destinationAirportId;
    }
    
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }
    
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public UUID getAircraftId() {
        return aircraftId;
    }
    
    public void setAircraftId(UUID aircraftId) {
        this.aircraftId = aircraftId;
    }
    
    public BigDecimal getBasePrice() {
        return basePrice;
    }
    
    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
    
    public Integer getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public FlightStatus getStatus() {
        return status;
    }
    
    public void setStatus(FlightStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

