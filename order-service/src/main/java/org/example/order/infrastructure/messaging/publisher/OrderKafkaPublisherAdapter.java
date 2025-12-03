package org.example.order.infrastructure.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.ports.output.OrderEventPublisher;
import org.example.order.domain.entity.Order;
import org.example.order.infrastructure.messaging.dto.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Output Adapter - Kafka Event Publisher
 * Implements OrderEventPublisher port from Application Layer
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderKafkaPublisherAdapter implements OrderEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String ORDER_RESERVATION_REQUESTED_TOPIC = "order.reservation.requested";
    private static final String ORDER_CREATED_TOPIC = "order.created";
    private static final String ORDER_CONFIRMED_TOPIC = "order.confirmed";
    private static final String ORDER_CANCELLED_TOPIC = "order.cancelled";

    @Override
    public void publishOrderReservationRequested(UUID orderId, UUID flightId, Integer quantityOfTickets) {
        try {
            log.info("Publishing OrderReservationRequested event: orderId={}, flightId={}, quantity={}", 
                    orderId, flightId, quantityOfTickets);
            
            OrderReservationRequestedEvent event = new OrderReservationRequestedEvent(orderId, flightId, quantityOfTickets);
            kafkaTemplate.send(ORDER_RESERVATION_REQUESTED_TOPIC, orderId.toString(), event);
            
            log.info("Successfully published OrderReservationRequested event for order: {}", orderId);
        } catch (Exception e) {
            log.error("Failed to publish OrderReservationRequested event for order: {}. Error: {}", 
                    orderId, e.getMessage(), e);
        }
    }

    @Override
    public void publishOrderCreated(UUID orderId, UUID customerId, BigDecimal totalPrice) {
        try {
            log.info("Publishing OrderCreated event: orderId={}, customerId={}, totalPrice={}", 
                    orderId, customerId, totalPrice);
            
            OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, totalPrice);
            kafkaTemplate.send(ORDER_CREATED_TOPIC, orderId.toString(), event);
            
            log.info("Successfully published OrderCreated event for order: {}", orderId);
        } catch (Exception e) {
            log.error("Failed to publish OrderCreated event for order: {}. Error: {}", 
                    orderId, e.getMessage(), e);
        }
    }

    @Override
    public void publishOrderConfirmed(Order order) {
        try {
            log.info("Publishing OrderConfirmed event for order: {}", order.getId());
            
            OrderConfirmedEvent event = new OrderConfirmedEvent(
                    order.getId(),
                    order.getCustomerId(),
                    order.getFlightId(),
                    order.getQuantityOfTickets()
            );
            kafkaTemplate.send(ORDER_CONFIRMED_TOPIC, order.getId().toString(), event);
            
            log.info("Successfully published OrderConfirmed event for order: {}", order.getId());
        } catch (Exception e) {
            log.error("Failed to publish OrderConfirmed event for order: {}. Error: {}", 
                    order.getId(), e.getMessage(), e);
        }
    }

    @Override
    public void publishOrderCancelled(Order order) {
        try {
            log.info("Publishing OrderCancelled event for order: {}", order.getId());
            
            OrderCancelledEvent event = new OrderCancelledEvent(
                    order.getId(),
                    order.getFlightId(),
                    order.getQuantityOfTickets()
            );
            kafkaTemplate.send(ORDER_CANCELLED_TOPIC, order.getId().toString(), event);
            
            log.info("Successfully published OrderCancelled event for order: {}", order.getId());
        } catch (Exception e) {
            log.error("Failed to publish OrderCancelled event for order: {}. Error: {}", 
                    order.getId(), e.getMessage(), e);
        }
    }
}

