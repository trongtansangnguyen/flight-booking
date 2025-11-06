package org.example.payment.application.ports.output;

import org.example.payment.domain.entity.Payment;
import org.example.payment.domain.valueobject.OrderId;

import java.util.Optional;

// Output Port
public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByOrderId(OrderId orderId);
}