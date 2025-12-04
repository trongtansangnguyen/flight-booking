package org.example.order.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.example.order.domain.exception.OrderDomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Order Domain Entity - Pure business logic
 * Domain Layer - Lombok is compile-time only, no runtime dependency
 */
@Getter
@Setter
public class Order {
    private UUID id;
    private UUID customerId;
    private UUID flightId;
    private Integer quantityOfTickets;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reservationExpiresAt; // Thời điểm hết hạn giữ chỗ

    // Default constructor for persistence layer
    // Factory method create() should be used for creating new orders
    public Order() {
    }

    /**
     * Factory method to create a new order with RESERVING status
     * @throws OrderDomainException if validation fails
     */
    public static Order create(UUID customerId, UUID flightId, Integer quantityOfTickets, BigDecimal totalPrice) {
        // Validate inputs
        Objects.requireNonNull(customerId, "Customer ID cannot be null");
        Objects.requireNonNull(flightId, "Flight ID cannot be null");
        Objects.requireNonNull(quantityOfTickets, "Quantity of tickets cannot be null");
        Objects.requireNonNull(totalPrice, "Total price cannot be null");
        
        if (quantityOfTickets <= 0) {
            throw new OrderDomainException("Quantity of tickets must be positive");
        }
        
        if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderDomainException("Total price must be positive");
        }
        
        Order order = new Order();
        order.customerId = customerId;
        order.flightId = flightId;
        order.quantityOfTickets = quantityOfTickets;
        order.totalPrice = totalPrice;
        order.status = OrderStatus.RESERVING;
        order.createdAt = LocalDateTime.now();
        order.updatedAt = LocalDateTime.now();
        return order;
    }

    /**
     * Business logic: Mark order as pending payment after seat reservation succeeds
     * Sets reservation expiry time (default 15 minutes from now)
     * @param reservationTimeoutMinutes Timeout in minutes for reservation (default 15)
     * @throws OrderDomainException if order status is invalid
     */
    public void markAsPendingPayment(int reservationTimeoutMinutes) {
        if (this.status != OrderStatus.RESERVING) {
            throw new OrderDomainException(
                    "Cannot mark as pending payment. Order status must be RESERVING. Current status: " + this.status);
        }
        this.status = OrderStatus.PENDING_PAYMENT;
        this.reservationExpiresAt = LocalDateTime.now().plusMinutes(reservationTimeoutMinutes);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Mark order as pending payment with default timeout (1 minute)
     * @throws OrderDomainException if order status is invalid
     */
    public void markAsPendingPayment() {
        markAsPendingPayment(1); // Default 1 minute
    }

    /**
     * Business logic: Mark order as failed (seat reservation failed)
     * @throws OrderDomainException if order status is invalid or reason is null
     */
    public void markAsFailed(String reason) {
        if (this.status != OrderStatus.RESERVING) {
            throw new OrderDomainException(
                    "Cannot mark as failed. Order status must be RESERVING. Current status: " + this.status);
        }
        Objects.requireNonNull(reason, "Failure reason cannot be null");
        if (reason.trim().isEmpty()) {
            throw new OrderDomainException("Failure reason cannot be empty");
        }
        this.status = OrderStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Mark order as confirmed after successful payment
     * @throws OrderDomainException if order status is invalid
     */
    public void markAsConfirmed() {
        if (this.status != OrderStatus.PENDING_PAYMENT) {
            throw new OrderDomainException(
                    "Cannot mark as confirmed. Order status must be PENDING_PAYMENT. Current status: " + this.status);
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Mark order as cancelled after payment failure or timeout
     * @param reason Reason for cancellation (optional)
     * @throws OrderDomainException if order status is invalid
     */
    public void markAsCancelled(String reason) {
        if (this.status != OrderStatus.PENDING_PAYMENT) {
            throw new OrderDomainException(
                    "Cannot mark as cancelled. Order status must be PENDING_PAYMENT. Current status: " + this.status);
        }
        this.status = OrderStatus.CANCELLED;
        if (reason != null && !reason.trim().isEmpty()) {
            this.failureReason = reason;
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Mark order as cancelled after payment failure
     * @throws OrderDomainException if order status is invalid
     */
    public void markAsCancelled() {
        markAsCancelled(null);
    }

    /**
     * Check if reservation has expired
     * @return true if reservation has expired, false otherwise
     */
    public boolean isReservationExpired() {
        if (this.status != OrderStatus.PENDING_PAYMENT) {
            return false; // Only PENDING_PAYMENT orders can expire
        }
        if (this.reservationExpiresAt == null) {
            return false; // No expiry set, consider as not expired
        }
        return LocalDateTime.now().isAfter(this.reservationExpiresAt);
    }

}

