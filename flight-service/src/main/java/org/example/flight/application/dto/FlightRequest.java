package org.example.flight.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

/**
 * Application Layer DTO - Request for Flight
 * Matches database design and API contract in docs
 */
public class FlightRequest {

	private String flightNumber;
	private UUID originAirportId;
	private UUID destinationAirportId;
	private LocalDateTime departureTime;
	private LocalDateTime arrivalTime;
	private UUID aircraftId;
	private BigDecimal basePrice;

	public FlightRequest() {
	}

	public FlightRequest(String flightNumber, UUID originAirportId, UUID destinationAirportId,
						 LocalDateTime departureTime, LocalDateTime arrivalTime,
						 UUID aircraftId, BigDecimal basePrice) {
		this.flightNumber = flightNumber;
		this.originAirportId = originAirportId;
		this.destinationAirportId = destinationAirportId;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.aircraftId = aircraftId;
		this.basePrice = basePrice;
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
}
