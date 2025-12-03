package org.example.payment.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Event DTO for Payment Successful - Outgoing message published by Payment Service
 * Simplified version for Order Service consumption
 */
public record PaymentSuccessfulEvent(
        @NotNull UUID orderId
) {
}

