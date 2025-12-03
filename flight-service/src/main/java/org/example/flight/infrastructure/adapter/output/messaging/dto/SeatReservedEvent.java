package org.example.flight.infrastructure.adapter.output.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Outgoing event to Order Service
 * Published when seats are successfully reserved
 */
public record SeatReservedEvent(
        @NotNull UUID orderId
) {
}

