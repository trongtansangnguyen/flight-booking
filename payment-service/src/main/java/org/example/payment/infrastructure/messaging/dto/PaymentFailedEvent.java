package org.example.payment.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Event DTO for Payment Failed - Outgoing message published by Payment Service
 * This belongs to infrastructure layer as it's used for external messaging
 */
public record PaymentFailedEvent(
        @NotNull UUID paymentId,
        @NotNull UUID orderId,
        @NotNull UUID customerId,
        @NotNull BigDecimal amount,
        @NotNull String status,
        List<String> failureReasons
) {}

