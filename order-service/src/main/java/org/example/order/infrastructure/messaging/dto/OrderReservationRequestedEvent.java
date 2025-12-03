package org.example.order.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * Outgoing event - Request seat reservation from Flight Service
 */
public record OrderReservationRequestedEvent(
        @NotNull UUID orderId,
        @NotNull UUID flightId,
        @NotNull @Positive Integer quantityOfTickets
) {
}

