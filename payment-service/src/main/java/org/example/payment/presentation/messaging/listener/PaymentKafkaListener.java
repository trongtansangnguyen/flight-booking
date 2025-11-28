package org.example.payment.presentation.messaging.listener;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.payment.application.ports.input.PaymentUseCase;
import org.example.payment.presentation.messaging.dto.FlightReservationFailedEvent;
import org.example.payment.presentation.messaging.dto.OrderCreatedEvent;
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
    @KafkaListener(topics = "order.created", groupId = "payment-group")
    public void handleOrderCreated(@Payload @Valid OrderCreatedEvent event) {
        log.info("Received OrderCreated event: {}", event.orderId());

        var command = eventMapper.orderCreatedEventToProcessCommand(event);
        paymentUseCase.processPayment(command);
    }

    /**
     * Lắng nghe sự kiện SAGA Compensation (Rubric B.8) từ Flight Service
     */
    @KafkaListener(topics = "flight.reservation.failed", groupId = "payment-group")
    public void handleFlightReservationFailed(@Payload @Valid FlightReservationFailedEvent event) {
        log.warn("Received FlightReservationFailed (compensation) event: {}", event.orderId());

        var command = eventMapper.flightFailedEventToRefundCommand(event);
        paymentUseCase.processRefund(command);
    }
}