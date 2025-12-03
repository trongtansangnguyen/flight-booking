package org.example.payment.infrastructure.messaging.mapper;

import org.example.payment.domain.entity.Payment;
import org.example.payment.infrastructure.messaging.dto.PaymentCompletedEvent;
import org.example.payment.infrastructure.messaging.dto.PaymentFailedEvent;
import org.example.payment.infrastructure.messaging.dto.PaymentRefundedEvent;
import org.example.payment.infrastructure.messaging.dto.PaymentSuccessfulEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Mapper to convert Payment Domain Entity to Event DTOs for messaging
 * This belongs to infrastructure layer as it maps domain entities to external message formats
 */
@Component
public class PaymentDomainEventMapper {

    public PaymentCompletedEvent paymentToPaymentCompletedEvent(Payment payment) {
        if (payment == null) {
            return null;
        }

        return new PaymentCompletedEvent(
                payment.getPaymentId() != null ? payment.getPaymentId().value() : null,
                payment.getOrderId().value(),
                payment.getCustomerId().value(),
                payment.getAmount().amount(),
                payment.getStatus() != null ? payment.getStatus().name() : null
        );
    }

    public PaymentFailedEvent paymentToPaymentFailedEvent(Payment payment) {
        if (payment == null) {
            return null;
        }

        return new PaymentFailedEvent(
                payment.getPaymentId() != null ? payment.getPaymentId().value() : null,
                payment.getOrderId().value(),
                payment.getCustomerId().value(),
                payment.getAmount().amount(),
                payment.getStatus() != null ? payment.getStatus().name() : null,
                payment.getFailureReasons() != null 
                    ? new ArrayList<>(payment.getFailureReasons()) 
                    : Collections.emptyList()
        );
    }

    public PaymentRefundedEvent paymentToPaymentRefundedEvent(Payment payment) {
        if (payment == null) {
            return null;
        }

        return new PaymentRefundedEvent(
                payment.getPaymentId() != null ? payment.getPaymentId().value() : null,
                payment.getOrderId().value(),
                payment.getCustomerId().value(),
                payment.getAmount().amount(),
                payment.getStatus() != null ? payment.getStatus().name() : null
        );
    }

    /**
     * Map Payment to PaymentSuccessfulEvent (simplified for Order Service)
     */
    public PaymentSuccessfulEvent paymentToPaymentSuccessfulEvent(Payment payment) {
        if (payment == null) {
            return null;
        }

        return new PaymentSuccessfulEvent(payment.getOrderId().value());
    }
}
