package org.example.payment.infrastructure.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.payment.application.ports.output.PaymentMessagePublisher;
import org.example.payment.domain.entity.Payment;
import org.example.payment.infrastructure.messaging.dto.PaymentFailedEvent;
import org.example.payment.infrastructure.messaging.dto.PaymentRefundedEvent;
import org.example.payment.infrastructure.messaging.dto.PaymentSuccessfulEvent;
import org.example.payment.infrastructure.messaging.mapper.PaymentDomainEventMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Output Adapter implementing PaymentMessagePublisher port
 * Publishes payment events to Kafka for SAGA orchestration
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentKafkaPublisherAdapter implements PaymentMessagePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentDomainEventMapper eventMapper;

    private static final String PAYMENT_SUCCESSFUL_TOPIC = "payment.successful";
    private static final String PAYMENT_FAILED_TOPIC = "payment.failed";
    private static final String PAYMENT_REFUNDED_TOPIC = "payment.refunded";

    @Override
    public void publishPaymentCompleted(Payment payment) {
        try {
            log.info("Publishing PaymentSuccessful event for order: {}", payment.getOrderId().value());
            PaymentSuccessfulEvent eventDto = eventMapper.paymentToPaymentSuccessfulEvent(payment);
            kafkaTemplate.send(PAYMENT_SUCCESSFUL_TOPIC, payment.getOrderId().value().toString(), eventDto);
            log.info("Successfully published PaymentSuccessful event for order: {}", payment.getOrderId().value());
        } catch (Exception e) {
            log.error("Failed to publish PaymentSuccessful event for order: {}. Error: {}", 
                    payment.getOrderId().value(), e.getMessage(), e);
            // In production, you might want to throw an exception or handle this differently
            // depending on your requirements
        }
    }

    @Override
    public void publishPaymentFailed(Payment payment) {
        try {
            log.info("Publishing PaymentFailed event for order: {}", payment.getOrderId().value());
            PaymentFailedEvent eventDto = eventMapper.paymentToPaymentFailedEvent(payment);
            kafkaTemplate.send(PAYMENT_FAILED_TOPIC, payment.getOrderId().value().toString(), eventDto);
            log.info("Successfully published PaymentFailed event for order: {}", payment.getOrderId().value());
        } catch (Exception e) {
            log.error("Failed to publish PaymentFailed event for order: {}. Error: {}", 
                    payment.getOrderId().value(), e.getMessage(), e);
        }
    }

    @Override
    public void publishPaymentRefunded(Payment payment) {
        try {
            log.info("Publishing PaymentRefunded event for order: {}", payment.getOrderId().value());
            PaymentRefundedEvent eventDto = eventMapper.paymentToPaymentRefundedEvent(payment);
            kafkaTemplate.send(PAYMENT_REFUNDED_TOPIC, payment.getOrderId().value().toString(), eventDto);
            log.info("Successfully published PaymentRefunded event for order: {}", payment.getOrderId().value());
        } catch (Exception e) {
            log.error("Failed to publish PaymentRefunded event for order: {}. Error: {}", 
                    payment.getOrderId().value(), e.getMessage(), e);
        }
    }
}