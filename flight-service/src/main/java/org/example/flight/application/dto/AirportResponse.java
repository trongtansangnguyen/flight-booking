package org.example.flight.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Application Layer DTO - Response for Airport
 * Matches database design: id, code, name, city, country, timestamps
 */
public class AirportResponse {
    
	private UUID id;
	private String code;
	private String name;
	private String city;
	private String country;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
    
	public AirportResponse() {
	}
    
	public AirportResponse(UUID id, String code, String name, String city, String country,
						   LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.city = city;
		this.country = country;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
    
	public UUID getId() {
		return id;
	}
    
	public void setId(UUID id) {
		this.id = id;
	}
    
	public String getCode() {
		return code;
	}
    
	public void setCode(String code) {
		this.code = code;
	}
    
	public String getName() {
		return name;
	}
    
	public void setName(String name) {
		this.name = name;
	}
    
	public String getCity() {
		return city;
	}
    
	public void setCity(String city) {
		this.city = city;
	}
    
	public String getCountry() {
		return country;
	}
    
	public void setCountry(String country) {
		this.country = country;
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
