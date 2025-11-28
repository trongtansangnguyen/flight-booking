package org.example.payment.presentation.messaging.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

// DTO "hợp đồng" message từ Kafka
public record OrderCreatedEvent(
        @NotNull UUID orderId,
        @NotNull UUID customerId,
        @NotNull @Positive BigDecimal totalAmount
) {}