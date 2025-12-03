package org.example.flight.infrastructure.adapter.output.messaging.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * Incoming event from Order Service
 * Request to reserve seats for an order
 */
public record OrderReservationRequestedEvent(
        @NotNull UUID orderId,
        @NotNull UUID flightId,
        @NotNull @Positive Integer quantityOfTickets
) {
}

