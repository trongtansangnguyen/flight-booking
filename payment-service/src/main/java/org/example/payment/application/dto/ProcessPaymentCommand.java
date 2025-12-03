package org.example.payment.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Command DTO for processing payment
 * Uses Java record for immutability and automatic validation
 */
public record ProcessPaymentCommand(
        @NotNull UUID orderId,
        @NotNull UUID customerId,
        @NotNull @Positive BigDecimal amount
) {
    // Record automatically provides constructor with all parameters
    // No need for @Builder annotation on records
}