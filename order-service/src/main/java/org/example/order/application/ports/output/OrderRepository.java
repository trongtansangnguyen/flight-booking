package org.example.order.application.ports.output;

import org.example.order.domain.entity.Order;

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
}

