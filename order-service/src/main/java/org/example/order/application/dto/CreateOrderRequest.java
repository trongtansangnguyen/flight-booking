package org.example.order.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for creating a new order
 */
public record CreateOrderRequest(
        @NotNull UUID customerId,
        @NotNull UUID flightId,
        @NotNull @Positive Integer quantityOfTickets,
        @NotNull @Positive BigDecimal totalPrice
) {
}

