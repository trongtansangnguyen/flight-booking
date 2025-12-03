package org.example.flight.infrastructure.adapter.output.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Incoming event from Order Service
 * Compensation transaction - release reserved seats
 */
public record OrderCancelledEvent(
        @NotNull UUID orderId,
        @NotNull UUID flightId,
        @NotNull Integer quantityOfTickets
) {
}

