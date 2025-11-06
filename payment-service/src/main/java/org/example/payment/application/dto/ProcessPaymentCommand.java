package org.example.payment.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

// Dùng record cho DTO bất biến, tự động có validation
public record ProcessPaymentCommand(
        @NotNull UUID orderId,
        @NotNull UUID customerId,
        @NotNull @Positive BigDecimal amount
) {
    @Builder
    public ProcessPaymentCommand {}
}