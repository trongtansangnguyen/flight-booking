package org.example.flight.application.port.input;

import java.util.UUID;

/**
 * Use Case Interface for Seat Reservation
 * Part of Saga Choreography workflow
 */
public interface SeatReservationUseCase {
    
    /**
     * Reserve seats for an order
     * @param orderId Order ID
     * @param flightId Flight ID
     * @param quantityOfTickets Number of tickets to reserve
     * @return true if reservation successful, false otherwise
     */
    boolean reserveSeats(UUID orderId, UUID flightId, Integer quantityOfTickets);
    
    /**
     * Release seats (compensation transaction)
     * @param orderId Order ID
     * @param flightId Flight ID
     * @param quantityOfTickets Number of tickets to release
     */
    void releaseSeats(UUID orderId, UUID flightId, Integer quantityOfTickets);
}

