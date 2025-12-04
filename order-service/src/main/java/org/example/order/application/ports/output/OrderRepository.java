package org.example.order.application.ports.output;

import org.example.order.domain.entity.Order;
import org.example.order.domain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output Port for Order Repository
 * Application Layer defines the interface
 */
public interface OrderRepository {
    Order save(Order order);
    
    Optional<Order> findById(UUID orderId);
    
    List<Order> findByCustomerId(UUID customerId);
    
    List<Order> findAll();
    
    /**
     * Find orders with specific status that have expired reservations
     * @param status Order status (typically PENDING_PAYMENT)
     * @param expiryTime Current time to compare against
     * @return List of expired orders
     */
    List<Order> findByStatusAndReservationExpiresAtBefore(OrderStatus status, LocalDateTime expiryTime);
}

