package org.example.flight.application.dto;

/**
 * Application Layer DTO - Request
 * Không có dependency vào framework cụ thể
 */
public class AircraftRequest {
    
    private String model;
    private String name;
    private Integer capacity;
    
    public AircraftRequest() {
    }
    
    public AircraftRequest(String model, String name, Integer capacity) {
        this.model = model;
        this.name = name;
        this.capacity = capacity;
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
}
