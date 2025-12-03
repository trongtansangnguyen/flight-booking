package org.example.flight.infrastructure.adapter.input.messaging.listener;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flight.application.port.input.SeatReservationUseCase;
import org.example.flight.application.port.output.SeatEventPublisher;
import org.example.flight.infrastructure.adapter.output.messaging.dto.OrderCancelledEvent;
import org.example.flight.infrastructure.adapter.output.messaging.dto.OrderReservationRequestedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka Listener for order events from Order Service
 * Handles Saga Choreography workflow:
 * - order.reservation.requested: Reserve seats
 * - order.cancelled: Release seats (compensation)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderKafkaListener {

    private final SeatReservationUseCase seatReservationUseCase;
    private final SeatEventPublisher seatEventPublisher;

    /**
     * Step 4-5: Listen for order reservation request
     * Reserve seats and publish result
     */
    @KafkaListener(
            topics = "order.reservation.requested",
            groupId = "flight-group",
            containerFactory = "orderReservationRequestedKafkaListenerContainerFactory")
    public void handleOrderReservationRequested(@Payload @Valid OrderReservationRequestedEvent event) {
        log.info("Received OrderReservationRequested event: orderId={}, flightId={}, quantity={}", 
                event.orderId(), event.flightId(), event.quantityOfTickets());
        
        try {
            // Attempt to reserve seats
            boolean success = seatReservationUseCase.reserveSeats(
                    event.orderId(), 
                    event.flightId(), 
                    event.quantityOfTickets()
            );
            
            if (success) {
                // Case A: Seats reserved successfully
                log.info("Seats reserved successfully for order: {}", event.orderId());
                seatEventPublisher.publishSeatReserved(event.orderId());
            } else {
                // Case B: Not enough seats available
                log.warn("Seat reservation failed for order: {}. Reason: sold_out", event.orderId());
                seatEventPublisher.publishSeatReservationFailed(event.orderId(), "sold_out");
            }
        } catch (Exception e) {
            log.error("Error processing seat reservation for order: {}. Error: {}", 
                    event.orderId(), e.getMessage(), e);
            seatEventPublisher.publishSeatReservationFailed(
                    event.orderId(), 
                    "Error: " + e.getMessage()
            );
        }
    }

    /**
     * Step 12: Listen for order cancellation (compensation transaction)
     * Release reserved seats
     */
    @KafkaListener(
            topics = "order.cancelled",
            groupId = "flight-group",
            containerFactory = "orderCancelledKafkaListenerContainerFactory")
    public void handleOrderCancelled(@Payload @Valid OrderCancelledEvent event) {
        log.info("Received OrderCancelled event: orderId={}, flightId={}, quantity={} (compensation transaction)", 
                event.orderId(), event.flightId(), event.quantityOfTickets());
        
        try {
            // Release seats (compensating transaction)
            seatReservationUseCase.releaseSeats(
                    event.orderId(), 
                    event.flightId(), 
                    event.quantityOfTickets()
            );
            log.info("Successfully released seats for cancelled order: {}", event.orderId());
        } catch (Exception e) {
            log.error("Error releasing seats for cancelled order: {}. Error: {}", 
                    event.orderId(), e.getMessage(), e);
        }
    }
}

