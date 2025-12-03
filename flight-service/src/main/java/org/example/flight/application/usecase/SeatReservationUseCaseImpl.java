package org.example.flight.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flight.application.port.input.SeatReservationUseCase;
import org.example.flight.application.port.output.FlightRepositoryPort;
import org.example.flight.domain.entity.Flight;
import org.example.flight.domain.exception.FlightNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case Implementation for Seat Reservation
 * Handles seat reservation and release (compensation) for Saga workflow
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeatReservationUseCaseImpl implements SeatReservationUseCase {
    
    private final FlightRepositoryPort flightRepositoryPort;
    
    @Override
    @Transactional
    public boolean reserveSeats(UUID orderId, UUID flightId, Integer quantityOfTickets) {
        log.info("Attempting to reserve {} seats for order {} on flight {}", 
                quantityOfTickets, orderId, flightId);
        
        Flight flight = flightRepositoryPort.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + flightId));
        
        // Check if enough seats available
        if (!flight.hasEnoughSeats(quantityOfTickets)) {
            log.warn("Not enough seats available. Required: {}, Available: {}", 
                    quantityOfTickets, flight.getAvailableSeats());
            return false;
        }
        
        // Reserve seats (lock them)
        flight.reserveSeats(quantityOfTickets);
        
        flightRepositoryPort.save(flight);
        log.info("Successfully reserved {} seats for order {} on flight {}. Remaining seats: {}", 
                quantityOfTickets, orderId, flightId, flight.getAvailableSeats());
        
        return true;
    }
    
    @Override
    @Transactional
    public void releaseSeats(UUID orderId, UUID flightId, Integer quantityOfTickets) {
        log.info("Releasing {} seats for order {} on flight {} (compensation transaction)", 
                quantityOfTickets, orderId, flightId);
        
        Flight flight = flightRepositoryPort.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + flightId));
        
        // Release seats (unlock them)
        flight.releaseSeats(quantityOfTickets);
        
        flightRepositoryPort.save(flight);
        log.info("Successfully released {} seats for order {} on flight {}. Available seats: {}", 
                quantityOfTickets, orderId, flightId, flight.getAvailableSeats());
    }
}

