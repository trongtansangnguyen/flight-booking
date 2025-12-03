package org.example.order.infrastructure.messaging.listener;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.ports.input.ProcessPaymentResultUseCase;
import org.example.order.infrastructure.messaging.dto.PaymentFailedEvent;
import org.example.order.infrastructure.messaging.dto.PaymentSuccessfulEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka Listener for payment results from Payment Service
 * Step 11 in Saga workflow
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResultListener {

    private final ProcessPaymentResultUseCase processPaymentResultUseCase;

    @KafkaListener(
            topics = "payment.successful",
            groupId = "order-group",
            containerFactory = "paymentSuccessfulKafkaListenerContainerFactory")
    public void handlePaymentSuccessful(@Payload @Valid PaymentSuccessfulEvent event) {
        log.info("Received PaymentSuccessful event: orderId={}", event.orderId());
        processPaymentResultUseCase.handlePaymentSuccessful(event.orderId());
    }

    @KafkaListener(
            topics = "payment.failed",
            groupId = "order-group",
            containerFactory = "paymentFailedKafkaListenerContainerFactory")
    public void handlePaymentFailed(@Payload @Valid PaymentFailedEvent event) {
        log.warn("Received PaymentFailed event: orderId={}", event.orderId());
        processPaymentResultUseCase.handlePaymentFailed(event.orderId());
    }
}

