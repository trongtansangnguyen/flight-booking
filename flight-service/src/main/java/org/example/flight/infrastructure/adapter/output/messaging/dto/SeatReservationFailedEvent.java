package org.example.flight.infrastructure.adapter.output.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Outgoing event to Order Service
 * Published when seat reservation fails
 */
public record SeatReservationFailedEvent(
        @NotNull UUID orderId,
        String reason
) {
}

