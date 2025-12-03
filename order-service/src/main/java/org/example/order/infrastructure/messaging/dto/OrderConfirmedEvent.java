package org.example.order.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Outgoing event - Order confirmed (after successful payment)
 */
public record OrderConfirmedEvent(
        @NotNull UUID orderId,
        @NotNull UUID customerId,
        @NotNull UUID flightId,
        @NotNull Integer quantityOfTickets
) {
}

