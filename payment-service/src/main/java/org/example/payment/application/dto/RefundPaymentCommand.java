package org.example.payment.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Command DTO for refunding payment (SAGA compensation)
 * Uses Java record for immutability and automatic validation
 */
public record RefundPaymentCommand(
        @NotNull UUID orderId
) {
    // Record automatically provides constructor with all parameters
    // No need for @Builder annotation on records
}