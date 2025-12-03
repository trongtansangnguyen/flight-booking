package org.example.order.infrastructure.messaging.listener;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.ports.input.ProcessSeatReservationResultUseCase;
import org.example.order.infrastructure.messaging.dto.SeatReservationFailedEvent;
import org.example.order.infrastructure.messaging.dto.SeatReservedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka Listener for seat reservation results from Flight Service
 * Step 6 in Saga workflow
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SeatReservationResultListener {

    private final ProcessSeatReservationResultUseCase processSeatReservationResultUseCase;

    @KafkaListener(
            topics = "seat.reserved",
            groupId = "order-group",
            containerFactory = "seatReservedKafkaListenerContainerFactory")
    public void handleSeatReserved(@Payload @Valid SeatReservedEvent event) {
        log.info("Received SeatReserved event: orderId={}", event.orderId());
        processSeatReservationResultUseCase.handleSeatReserved(event.orderId());
    }

    @KafkaListener(
            topics = "seat.reservation.failed",
            groupId = "order-group",
            containerFactory = "seatReservationFailedKafkaListenerContainerFactory")
    public void handleSeatReservationFailed(@Payload @Valid SeatReservationFailedEvent event) {
        log.warn("Received SeatReservationFailed event: orderId={}, reason={}", 
                event.orderId(), event.reason());
        processSeatReservationResultUseCase.handleSeatReservationFailed(event.orderId(), event.reason());
    }
}

