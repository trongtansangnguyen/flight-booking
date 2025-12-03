package org.example.order.application.dto;

import org.example.order.domain.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for order response
 */
public record OrderResponse(
        UUID id,
        UUID customerId,
        UUID flightId,
        Integer quantityOfTickets,
        BigDecimal totalPrice,
        OrderStatus status,
        String failureReason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

