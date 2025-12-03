package org.example.flight.application.port.output;

import java.util.UUID;

/**
 * Output Port for publishing seat reservation events to Kafka
 */
public interface SeatEventPublisher {
    
    /**
     * Publish event when seats are successfully reserved
     * Topic: seat.reserved
     */
    void publishSeatReserved(UUID orderId);
    
    /**
     * Publish event when seat reservation fails
     * Topic: seat.reservation.failed
     */
    void publishSeatReservationFailed(UUID orderId, String reason);
}

