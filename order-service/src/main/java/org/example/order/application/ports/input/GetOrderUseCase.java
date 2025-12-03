package org.example.order.application.ports.input;

import org.example.order.application.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

/**
 * Input Port for querying orders
 */
public interface GetOrderUseCase {
    
    /**
     * Get order by ID
     */
    OrderResponse getOrderById(UUID orderId);
    
    /**
     * Get all orders for a customer
     */
    List<OrderResponse> getOrdersByCustomerId(UUID customerId);
    
    /**
     * Get all orders
     */
    List<OrderResponse> getAllOrders();
}

