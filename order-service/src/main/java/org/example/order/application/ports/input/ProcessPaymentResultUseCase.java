package org.example.order.application.ports.input;

import java.util.UUID;

/**
 * Input Port for processing payment result from Payment Service
 */
public interface ProcessPaymentResultUseCase {
    
    /**
     * Process successful payment
     * Updates order status to CONFIRMED and publishes order.confirmed event
     */
    void handlePaymentSuccessful(UUID orderId);
    
    /**
     * Process failed payment
     * Updates order status to CANCELLED and publishes order.cancelled event
     */
    void handlePaymentFailed(UUID orderId);
}

