package org.example.flight.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.example.flight.domain.entity.FlightStatus;

/**
 * Application Layer DTO - Response for Flight
 * Bao gồm thông tin nested của Airport và Aircraft
 */
public class FlightResponse {
    
    private UUID id;
    private String flightNumber;
    
    // Origin Airport Info
    private UUID originAirportId;
    private String originCode;
    private String originName;
    private String originCity;
    
    // Destination Airport Info
    private UUID destinationAirportId;
    private String destinationCode;
    private String destinationName;
    private String destinationCity;
    
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    
    // Aircraft Info
    private UUID aircraftId;
    private String aircraftModel;
    private String aircraftName;
    private Integer aircraftCapacity;
    
    private BigDecimal basePrice;
    private Integer availableSeats;
    private FlightStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public FlightResponse() {
    }
    
    public FlightResponse(UUID id, String flightNumber, UUID originAirportId, String originCode,
                         String originName, String originCity, UUID destinationAirportId,
                         String destinationCode, String destinationName, String destinationCity,
                         LocalDateTime departureTime, LocalDateTime arrivalTime, UUID aircraftId,
                         String aircraftModel, String aircraftName, Integer aircraftCapacity,
                         BigDecimal basePrice, Integer availableSeats, FlightStatus status,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.originAirportId = originAirportId;
        this.originCode = originCode;
        this.originName = originName;
        this.originCity = originCity;
        this.destinationAirportId = destinationAirportId;
        this.destinationCode = destinationCode;
        this.destinationName = destinationName;
        this.destinationCity = destinationCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraftId = aircraftId;
        this.aircraftModel = aircraftModel;
        this.aircraftName = aircraftName;
        this.aircraftCapacity = aircraftCapacity;
        this.basePrice = basePrice;
        this.availableSeats = availableSeats;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    
    public String getOriginCode() {
        return originCode;
    }
    
    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }
    
    public String getOriginName() {
        return originName;
    }
    
    public void setOriginName(String originName) {
        this.originName = originName;
    }
    
    public String getOriginCity() {
        return originCity;
    }
    
    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }
    
    public UUID getDestinationAirportId() {
        return destinationAirportId;
    }
    
    public void setDestinationAirportId(UUID destinationAirportId) {
        this.destinationAirportId = destinationAirportId;
    }
    
    public String getDestinationCode() {
        return destinationCode;
    }
    
    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }
    
    public String getDestinationName() {
        return destinationName;
    }
    
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
    
    public String getDestinationCity() {
        return destinationCity;
    }
    
    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
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
    
    public String getAircraftModel() {
        return aircraftModel;
    }
    
    public void setAircraftModel(String aircraftModel) {
        this.aircraftModel = aircraftModel;
    }
    
    public String getAircraftName() {
        return aircraftName;
    }
    
    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }
    
    public Integer getAircraftCapacity() {
        return aircraftCapacity;
    }
    
    public void setAircraftCapacity(Integer aircraftCapacity) {
        this.aircraftCapacity = aircraftCapacity;
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

