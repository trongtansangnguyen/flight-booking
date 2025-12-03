package org.example.order.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Event DTO for payment success
 * Incoming event from Payment Service
 */
public record PaymentSuccessfulEvent(
        @NotNull UUID orderId
) {
}

