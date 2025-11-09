package org.example.flight.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

/**
 * Domain Entity - Flight
 * Matches database design: flight_id, flight_number, airports, times, aircraft, pricing, seats, status
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
    private String status;  // SCHEDULED, DELAYED, CANCELLED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Flight() {
    }
    
    public Flight(UUID id, String flightNumber,
                  UUID originAirportId, UUID destinationAirportId,
                  LocalDateTime departureTime, LocalDateTime arrivalTime,
                  UUID aircraftId, BigDecimal basePrice,
                  Integer availableSeats, String status,
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
    public static Flight create(String flightNumber,
                              UUID originAirportId, UUID destinationAirportId,
                              LocalDateTime departureTime, LocalDateTime arrivalTime,
                              UUID aircraftId, BigDecimal basePrice) {
        Flight flight = new Flight();
        flight.flightNumber = flightNumber;
        flight.originAirportId = originAirportId;
        flight.destinationAirportId = destinationAirportId;
        flight.departureTime = departureTime;
        flight.arrivalTime = arrivalTime;
        flight.aircraftId = aircraftId;
        flight.basePrice = basePrice;
        flight.status = "SCHEDULED";  // Default status
        // availableSeats will be set by infrastructure layer based on aircraft.capacity
        flight.createdAt = LocalDateTime.now();
        flight.updatedAt = LocalDateTime.now();
        return flight;
    }
    
    // Business logic methods
    public void updateDetails(String flightNumber,
                            UUID originAirportId, UUID destinationAirportId,
                            LocalDateTime departureTime, LocalDateTime arrivalTime,
                            UUID aircraftId, BigDecimal basePrice) {
        this.flightNumber = flightNumber;
        this.originAirportId = originAirportId;
        this.destinationAirportId = destinationAirportId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraftId = aircraftId;
        this.basePrice = basePrice;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setStatus(String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid flight status: " + status);
        }
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isAvailable() {
        return "SCHEDULED".equals(status) && availableSeats > 0;
    }
    
    public void decreaseAvailableSeats() {
        if (availableSeats <= 0) {
            throw new IllegalStateException("No available seats");
        }
        availableSeats--;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void increaseAvailableSeats() {
        // Assuming we'll validate against aircraft capacity in service layer
        availableSeats++;
        this.updatedAt = LocalDateTime.now();
    }
    
    private boolean isValidStatus(String status) {
        return "SCHEDULED".equals(status) ||
               "DELAYED".equals(status) ||
               "CANCELLED".equals(status);
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
    
    public String getStatus() {
        return status;
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