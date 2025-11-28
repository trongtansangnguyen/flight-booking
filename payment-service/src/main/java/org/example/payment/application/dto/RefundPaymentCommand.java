package org.example.payment.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

public record RefundPaymentCommand(
        @NotNull UUID orderId
) {
    @Builder
    public RefundPaymentCommand {}
}