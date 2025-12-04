package org.example.order.application.ports.input;

/**
 * Input Port for cancelling expired reservations
 * Application Layer - Use Case Interface
 */
public interface CancelExpiredReservationsUseCase {
    
    /**
     * Cancel all expired reservations (orders with PENDING_PAYMENT status
     * that have passed their reservation expiry time)
     * 
     * @return Number of cancelled orders
     */
    int cancelExpiredReservations();
}

