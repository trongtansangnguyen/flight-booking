package org.example.payment.application.ports.output;

import org.example.payment.domain.entity.Payment;

public interface PaymentMessagePublisher {
    void publishPaymentCompleted(Payment payment);
    void publishPaymentFailed(Payment payment);
    void publishPaymentRefunded(Payment payment);
}