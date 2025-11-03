package org.example.flight.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Entity - Pure business logic, no framework dependencies
 * Domain Layer không phụ thuộc vào bất kỳ layer nào
 */
public class Aircraft {
    
    private UUID id;
    private String model;
    private String name;
    private Integer capacity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor
    public Aircraft() {
    }
    
    public Aircraft(UUID id, String model, String name, Integer capacity, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.model = model;
        this.name = name;
        this.capacity = capacity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Factory method for creating new aircraft
    public static Aircraft create(String model, String name, Integer capacity) {
        Aircraft aircraft = new Aircraft();
        aircraft.model = model;
        aircraft.name = name;
        aircraft.capacity = capacity;
        aircraft.createdAt = LocalDateTime.now();
        aircraft.updatedAt = LocalDateTime.now();
        return aircraft;
    }
    
    // Business logic methods
    public void updateDetails(String model, String name, Integer capacity) {
        this.model = model;
        this.name = name;
        this.capacity = capacity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasCapacityFor(int passengers) {
        return this.capacity >= passengers;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
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
