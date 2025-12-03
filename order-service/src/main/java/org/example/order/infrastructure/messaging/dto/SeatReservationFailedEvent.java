package org.example.order.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Event DTO for seat reservation failure
 * Incoming event from Flight Service
 */
public record SeatReservationFailedEvent(
        @NotNull UUID orderId,
        String reason
) {
}

