package org.example.order.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Outgoing event - Order refund requested
 * Published when order is cancelled after payment was completed
 * Triggers refund in Payment Service
 */
public record OrderRefundRequestedEvent(
        @NotNull UUID orderId,
        @NotNull UUID customerId,
        @NotNull @Positive BigDecimal amount
) {
}

