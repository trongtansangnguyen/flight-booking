package org.example.order.application.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO for cancel order request
 */
public record CancelOrderRequest(
        @NotNull UUID customerId
) {
}

