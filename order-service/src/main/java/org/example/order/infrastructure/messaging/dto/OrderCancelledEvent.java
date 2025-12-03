package org.example.order.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Outgoing event - Order cancelled (after payment failure)
 * Triggers compensation transaction in Flight Service
 */
public record OrderCancelledEvent(
        @NotNull UUID orderId,
        @NotNull UUID flightId,
        @NotNull Integer quantityOfTickets
) {
}

