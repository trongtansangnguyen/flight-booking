package org.example.order.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.ports.input.ProcessSeatReservationResultUseCase;
import org.example.order.application.ports.output.OrderEventPublisher;
import org.example.order.application.ports.output.OrderRepository;
import org.example.order.domain.entity.Order;
import org.example.order.domain.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case Implementation for processing seat reservation results
 * Step 6 in the Saga workflow:
 * - If seat.reserved: update to PENDING_PAYMENT, publish order.created
 * - If seat.reservation.failed: update to FAILED
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessSeatReservationResultUseCaseImpl implements ProcessSeatReservationResultUseCase {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;
    
    @Value("${order.reservation.timeout.minutes:2}")
    private int reservationTimeoutMinutes;

    @Override
    @Transactional
    public void handleSeatReserved(UUID orderId) {
        log.info("Processing seat reservation success for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        // Update order status to PENDING_PAYMENT with reservation expiry time
        order.markAsPendingPayment(reservationTimeoutMinutes);
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Order {} marked as PENDING_PAYMENT with reservation expiry at: {}", 
                orderId, updatedOrder.getReservationExpiresAt());

        // Publish order.created event (Step 6 - triggers payment service)
        orderEventPublisher.publishOrderCreated(
                updatedOrder.getId(),
                updatedOrder.getCustomerId(),
                updatedOrder.getTotalPrice());

        log.info("Order {} updated to PENDING_PAYMENT, published order.created event", orderId);
    }

    @Override
    @Transactional
    public void handleSeatReservationFailed(UUID orderId, String reason) {
        log.warn("Processing seat reservation failure for order: {}, reason: {}", orderId, reason);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        // Update order status to FAILED
        order.markAsFailed(reason);
        orderRepository.save(order);

        log.info("Order {} marked as FAILED due to: {}", orderId, reason);
    }
}

