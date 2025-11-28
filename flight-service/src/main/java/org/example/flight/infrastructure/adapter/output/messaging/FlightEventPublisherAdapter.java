package org.example.flight.infrastructure.adapter.output.messaging;

import lombok.extern.slf4j.Slf4j;
import org.example.flight.application.dto.event.SeatReservationFailedEvent;
import org.example.flight.application.dto.event.SeatReservedEvent;
import org.example.flight.application.port.output.FlightEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("!test")
@Component
public class FlightEventPublisherAdapter implements FlightEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public FlightEventPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishSeatReserved(SeatReservedEvent event) {
        log.info("Publishing seat.reserved bookingId={} flightId={} seats={}",
            event.getBookingId(), event.getFlightId(), event.getSeatCount());
        kafkaTemplate.send("seat.reserved", event);
    }

    @Override
    public void publishSeatReservationFailed(SeatReservationFailedEvent event) {
        log.warn("Publishing seat.reservation.failed bookingId={} flightId={} seats={} reason={}",
            event.getBookingId(), event.getFlightId(), event.getSeatCount(), event.getReason());
        kafkaTemplate.send("seat.reservation.failed", event);
    }
}

