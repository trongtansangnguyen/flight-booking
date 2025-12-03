package org.example.order.application.mapper;

import org.example.order.application.dto.OrderResponse;
import org.example.order.domain.entity.Order;
import org.springframework.stereotype.Component;

/**
 * Mapper utility for Order domain entity to DTOs
 * Application Layer - Can be used across use cases
 */
@Component
public class OrderMapper {

    /**
     * Maps Order domain entity to OrderResponse DTO
     */
    public OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getFlightId(),
                order.getQuantityOfTickets(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getFailureReason(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}

