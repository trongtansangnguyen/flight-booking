package org.example.flight.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Application Layer DTO - Response
 * Không có dependency vào framework cụ thể
 */
public class AircraftResponse {
    
    private UUID id;
    private String model;
    private String name;
    private Integer capacity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public AircraftResponse() {
    }
    
    public AircraftResponse(UUID id, String model, String name, Integer capacity, 
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.model = model;
        this.name = name;
        this.capacity = capacity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
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
