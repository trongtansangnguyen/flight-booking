package org.example.payment.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response DTO for customer credit information
 */
public record CustomerCreditResponse(
        UUID customerId,
        BigDecimal creditLimit,
        BigDecimal currentBalance
) {
}

