package org.example.payment.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.payment.domain.exception.PaymentDomainException;
import org.example.payment.domain.valueobject.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Payment {
    private PaymentId paymentId;
    private final OrderId orderId;
    private final CustomerId customerId;
    private final Money amount;

    private PaymentStatus status;
    private List<String> failureReasons;

    /**
     * Initialize a new payment with validation
     * @throws PaymentDomainException if required fields are null
     */
    public void initializePayment() {
        validatePaymentInvariants();
        this.status = PaymentStatus.PENDING;
        if (this.failureReasons == null) {
            this.failureReasons = new ArrayList<>();
        }
    }

    /**
     * Mark payment as completed
     * @throws PaymentDomainException if payment is not in valid state for completion
     */
    public void complete() {
        if (this.status != PaymentStatus.PENDING) {
            throw new PaymentDomainException(
                    "Cannot complete payment. Payment must be in PENDING status. Current status: " + this.status);
        }
        this.status = PaymentStatus.COMPLETED;
    }

    /**
     * Mark payment as failed with reason
     * @param reason failure reason (must not be null or empty)
     * @throws PaymentDomainException if reason is invalid
     */
    public void fail(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new PaymentDomainException("Failure reason cannot be null or empty");
        }
        if (this.status == PaymentStatus.COMPLETED || this.status == PaymentStatus.REFUNDED) {
            throw new PaymentDomainException(
                    "Cannot fail payment. Payment is already " + this.status + ". Order ID: " + 
                    (this.orderId != null ? this.orderId.value() : "unknown"));
        }
        this.status = PaymentStatus.FAILED;
        if (this.failureReasons == null) {
            this.failureReasons = new ArrayList<>();
        }
        this.failureReasons.add(reason);
    }

    /**
     * Mark payment as refunded
     * @throws PaymentDomainException if payment is not in valid state for refund
     */
    public void refund() {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new PaymentDomainException(
                    "Cannot refund payment. Payment must be COMPLETED. Current status: " + this.status);
        }
        this.status = PaymentStatus.REFUNDED;
    }

    /**
     * Reset payment for retry (from FAILED to PENDING)
     * Used when user manually retries payment after failure
     * @throws PaymentDomainException if payment is not in FAILED status
     */
    public void resetForRetry() {
        if (this.status != PaymentStatus.FAILED) {
            throw new PaymentDomainException(
                    "Cannot reset payment for retry. Payment must be in FAILED status. Current status: " + this.status);
        }
        this.status = PaymentStatus.PENDING;
        // Clear failure reasons for retry
        if (this.failureReasons != null) {
            this.failureReasons.clear();
        }
    }

    /**
     * Validate payment invariants
     * @throws PaymentDomainException if invariants are violated
     */
    private void validatePaymentInvariants() {
        if (this.orderId == null) {
            throw new PaymentDomainException("Order ID cannot be null");
        }
        if (this.customerId == null) {
            throw new PaymentDomainException("Customer ID cannot be null");
        }
        if (this.amount == null) {
            throw new PaymentDomainException("Amount cannot be null");
        }
    }
}