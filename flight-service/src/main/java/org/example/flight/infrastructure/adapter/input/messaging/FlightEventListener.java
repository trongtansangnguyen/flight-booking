package org.example.flight.infrastructure.adapter.input.messaging;

import lombok.extern.slf4j.Slf4j;
import org.example.flight.application.dto.event.OrderCancelledEvent;
import org.example.flight.application.dto.event.OrderReservationRequestedEvent;
import org.example.flight.application.port.input.FlightUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FlightEventListener {

    private final FlightUseCase flightUseCase;

    public FlightEventListener(FlightUseCase flightUseCase) {
        this.flightUseCase = flightUseCase;
    }

    @KafkaListener(topics = "order.reservation.requested", groupId = "flight-service-group")
    public void handleReservationRequested(OrderReservationRequestedEvent event) {
        log.info("Received reservation request bookingId={} flightId={} seats={}",
            event.getBookingId(), event.getFlightId(), event.getSeatCount());
        flightUseCase.handleReservationRequest(event);
    }

    @KafkaListener(topics = "order.cancelled", groupId = "flight-service-group")
    public void handleOrderCancelled(OrderCancelledEvent event) {
        log.info("Received order cancelled bookingId={} flightId={} seats={}",
            event.getBookingId(), event.getFlightId(), event.getSeatCount());
        flightUseCase.handleCompensation(event);
    }
}

