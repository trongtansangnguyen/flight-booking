package org.example.payment.presentation.messaging.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

// DTO cho sự kiện SAGA Compensation từ FlightService
public record FlightReservationFailedEvent(
        @NotNull UUID orderId,
        String reason // Lý do thất bại, ví dụ: "INSUFFICIENT_SEATS"
) {}