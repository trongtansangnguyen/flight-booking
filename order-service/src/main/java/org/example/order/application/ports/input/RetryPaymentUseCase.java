package org.example.order.application.ports.input;

import java.util.UUID;

/**
 * Input Port for manual payment retry
 * Application Layer - Use Case Interface
 * 
 * Allows user to manually retry payment for an order
 */
public interface RetryPaymentUseCase {
    
    /**
     * Retry payment for an order
     * Only works if order is in PENDING_PAYMENT status and not expired
     * 
     * @param orderId Order ID to retry payment for
     * @throws org.example.order.domain.exception.OrderNotFoundException if order not found
     * @throws org.example.order.domain.exception.OrderDomainException if order is not in valid state for retry
     */
    void retryPayment(UUID orderId);
}

