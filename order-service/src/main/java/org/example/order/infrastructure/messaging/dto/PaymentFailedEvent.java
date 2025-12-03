package org.example.order.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Event DTO for payment failure
 * Incoming event from Payment Service
 */
public record PaymentFailedEvent(
        @NotNull UUID orderId
) {
}

