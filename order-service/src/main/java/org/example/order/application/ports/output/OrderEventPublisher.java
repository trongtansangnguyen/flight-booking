package org.example.order.application.ports.output;

import org.example.order.domain.entity.Order;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Output Port for publishing order events to Kafka
 * Application Layer defines the interface
 */
public interface OrderEventPublisher {
    
    /**
     * Publish event when order reservation is requested
     * Topic: order.reservation.requested
     */
    void publishOrderReservationRequested(UUID orderId, UUID flightId, Integer quantityOfTickets);
    
    /**
     * Publish event when order is created (after seat reservation succeeds)
     * Topic: order.created
     */
    void publishOrderCreated(UUID orderId, UUID customerId, BigDecimal totalPrice);
    
    /**
     * Publish event when order is confirmed (after successful payment)
     * Topic: order.confirmed
     */
    void publishOrderConfirmed(Order order);
    
    /**
     * Publish event when order is cancelled (after payment failure)
     * Topic: order.cancelled
     */
    void publishOrderCancelled(Order order);
}

