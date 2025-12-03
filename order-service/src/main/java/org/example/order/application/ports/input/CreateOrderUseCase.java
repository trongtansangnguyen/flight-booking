package org.example.order.application.ports.input;

import org.example.order.application.dto.CreateOrderRequest;
import org.example.order.application.dto.OrderResponse;

/**
 * Input Port for creating a new order
 */
public interface CreateOrderUseCase {
    OrderResponse createOrder(CreateOrderRequest request);
}

