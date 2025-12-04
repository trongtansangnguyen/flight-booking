package org.example.payment.presentation.messaging.listener;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.payment.application.ports.input.PaymentUseCase;
import org.example.payment.presentation.messaging.dto.OrderCreatedEvent;
import org.example.payment.presentation.messaging.dto.OrderRefundRequestedEvent;
import org.example.payment.presentation.messaging.mapper.PaymentEventMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentKafkaListener {

    private final PaymentUseCase paymentUseCase;
    private final PaymentEventMapper eventMapper;

    /**
     * Lắng nghe sự kiện SAGA (Happy Path)
     */
    @KafkaListener(
            topics = "order.created",
            groupId = "payment-group",
            containerFactory = "orderCreatedKafkaListenerContainerFactory")
    public void handleOrderCreated(@Payload @Valid OrderCreatedEvent event) {
        log.info("Received OrderCreated event: orderId={}, customerId={}, totalPrice={}", 
                event.orderId(), event.customerId(), event.totalPrice());

        if (event.customerId() == null) {
            log.error("OrderCreated event has null customerId for order: {}", event.orderId());
            throw new IllegalArgumentException("Customer ID cannot be null in OrderCreated event");
        }

        var command = eventMapper.orderCreatedEventToProcessCommand(event);
        paymentUseCase.processPayment(command);
    }

    /**
     * Lắng nghe sự kiện refund request từ Order Service
     * Triggered when order is cancelled after payment was completed
     */
    @KafkaListener(
            topics = "order.refund.requested",
            groupId = "payment-group",
            containerFactory = "orderRefundRequestedKafkaListenerContainerFactory")
    public void handleOrderRefundRequested(@Payload @Valid OrderRefundRequestedEvent event) {
        log.warn("Received OrderRefundRequested event: orderId={}, customerId={}, amount={}", 
                event.orderId(), event.customerId(), event.amount());

        var command = eventMapper.orderRefundRequestedEventToRefundCommand(event);
        paymentUseCase.processRefund(command);
    }
}