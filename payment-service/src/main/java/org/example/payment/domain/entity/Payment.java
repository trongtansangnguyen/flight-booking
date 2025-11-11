package org.example.payment.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
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

    public void initializePayment() {
        this.status = PaymentStatus.PENDING;
        this.failureReasons = new ArrayList<>();
    }

    public void complete() {
        this.status = PaymentStatus.COMPLETED;
    }

    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReasons.add(reason);
    }

    public void refund() {
        this.status = PaymentStatus.REFUNDED;
    }
}