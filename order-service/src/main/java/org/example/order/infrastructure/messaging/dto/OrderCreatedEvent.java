package org.example.order.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Outgoing event - Order created (after seat reservation succeeds)
 * Triggers Payment Service.
 * <p>
 * Contract:
 * - orderId: ID của order
 * - customerId: ID khách hàng (để Payment Service biết trừ tiền ai)
 * - totalPrice: tổng số tiền cần thanh toán
 */
public record OrderCreatedEvent(
        @NotNull UUID orderId,
        @NotNull UUID customerId,
        @NotNull @Positive BigDecimal totalPrice
) {
}

