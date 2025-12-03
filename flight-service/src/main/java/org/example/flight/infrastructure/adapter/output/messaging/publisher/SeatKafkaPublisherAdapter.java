package org.example.flight.infrastructure.adapter.output.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flight.application.port.output.SeatEventPublisher;
import org.example.flight.infrastructure.adapter.output.messaging.dto.SeatReservationFailedEvent;
import org.example.flight.infrastructure.adapter.output.messaging.dto.SeatReservedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Output Adapter - Kafka Event Publisher for Seat Reservation
 * Implements SeatEventPublisher port from Application Layer
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SeatKafkaPublisherAdapter implements SeatEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String SEAT_RESERVED_TOPIC = "seat.reserved";
    private static final String SEAT_RESERVATION_FAILED_TOPIC = "seat.reservation.failed";

    @Override
    public void publishSeatReserved(UUID orderId) {
        try {
            log.info("Publishing SeatReserved event: orderId={}", orderId);
            
            SeatReservedEvent event = new SeatReservedEvent(orderId);
            kafkaTemplate.send(SEAT_RESERVED_TOPIC, orderId.toString(), event);
            
            log.info("Successfully published SeatReserved event for order: {}", orderId);
        } catch (Exception e) {
            log.error("Failed to publish SeatReserved event for order: {}. Error: {}", 
                    orderId, e.getMessage(), e);
        }
    }

    @Override
    public void publishSeatReservationFailed(UUID orderId, String reason) {
        try {
            log.warn("Publishing SeatReservationFailed event: orderId={}, reason={}", orderId, reason);
            
            SeatReservationFailedEvent event = new SeatReservationFailedEvent(orderId, reason);
            kafkaTemplate.send(SEAT_RESERVATION_FAILED_TOPIC, orderId.toString(), event);
            
            log.info("Successfully published SeatReservationFailed event for order: {}", orderId);
        } catch (Exception e) {
            log.error("Failed to publish SeatReservationFailed event for order: {}. Error: {}", 
                    orderId, e.getMessage(), e);
        }
    }
}

