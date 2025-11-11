package org.example.payment.infrastructure.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.payment.application.ports.output.PaymentMessagePublisher;
import org.example.payment.domain.entity.Payment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentKafkaPublisherAdapter implements PaymentMessagePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    // (Bạn sẽ cần một Mapper để chuyển Domain Entity -> Event DTO cho Kafka)

    private static final String PAYMENT_COMPLETED_TOPIC = "payment.completed";
    private static final String PAYMENT_FAILED_TOPIC = "payment.failed";
    private static final String PAYMENT_REFUNDED_TOPIC = "payment.refunded";

    @Override
    public void publishPaymentCompleted(Payment payment) {
        log.info("Publishing PaymentCompleted event for order: {}", payment.getOrderId().value());
        // Object eventDto = mapper.paymentToPaymentCompletedEvent(payment);
        // kafkaTemplate.send(PAYMENT_COMPLETED_TOPIC, eventDto);
    }

    @Override
    public void publishPaymentFailed(Payment payment) {
        log.info("Publishing PaymentFailed event for order: {}", payment.getOrderId().value());
        // Object eventDto = mapper.paymentToPaymentFailedEvent(payment);
        // kafkaTemplate.send(PAYMENT_FAILED_TOPIC, eventDto);
    }

    @Override
    public void publishPaymentRefunded(Payment payment) {
        log.info("Publishing PaymentRefunded event for order: {}", payment.getOrderId().value());
        // Object eventDto = mapper.paymentToPaymentRefundedEvent(payment);
        // kafkaTemplate.send(PAYMENT_REFUNDED_TOPIC, eventDto);
    }
}