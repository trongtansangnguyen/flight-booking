package org.example.order.application.ports.input;

import java.util.UUID;

/**
 * Input Port for cancelling an order
 * Application Layer - Use Case Interface
 * 
 * Allows customer to manually cancel an order
 * Validates that cancellation is at least 24 hours before flight departure
 * Triggers refund if payment was completed
 */
public interface CancelOrderUseCase {
    
    /**
     * Cancel an order
     * Validates:
     * - Order must be in PENDING_PAYMENT or CONFIRMED status
     * - Cancellation must be at least 24 hours before flight departure
     * 
     * If payment was completed, triggers refund
     * Publishes order.cancelled event to release seats
     * 
     * @param orderId Order ID to cancel
     * @param customerId Customer ID (for authorization - must match order customer)
     * @throws org.example.order.domain.exception.OrderNotFoundException if order not found
     * @throws org.example.order.domain.exception.OrderDomainException if order cannot be cancelled
     */
    void cancelOrder(UUID orderId, UUID customerId);
}

