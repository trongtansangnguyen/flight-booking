package org.example.order.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Event DTO for seat reservation success
 * Incoming event from Flight Service
 */
public record SeatReservedEvent(
        @NotNull UUID orderId
) {
}

