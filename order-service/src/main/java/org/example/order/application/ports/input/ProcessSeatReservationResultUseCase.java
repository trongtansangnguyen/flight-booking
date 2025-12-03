package org.example.order.application.ports.input;

import java.util.UUID;

/**
 * Input Port for processing seat reservation result from Flight Service
 */
public interface ProcessSeatReservationResultUseCase {
    
    /**
     * Process successful seat reservation
     * Updates order status to PENDING_PAYMENT and publishes order.created event
     */
    void handleSeatReserved(UUID orderId);
    
    /**
     * Process failed seat reservation
     * Updates order status to FAILED
     */
    void handleSeatReservationFailed(UUID orderId, String reason);
}

