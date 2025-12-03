package org.example.order.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.ports.input.ProcessPaymentResultUseCase;
import org.example.order.application.ports.output.OrderEventPublisher;
import org.example.order.application.ports.output.OrderRepository;
import org.example.order.domain.entity.Order;
import org.example.order.domain.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case Implementation for processing payment results
 * Step 11 in the Saga workflow:
 * - If payment.successful: update to CONFIRMED, publish order.confirmed
 * - If payment.failed: update to CANCELLED, publish order.cancelled
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessPaymentResultUseCaseImpl implements ProcessPaymentResultUseCase {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    @Transactional
    public void handlePaymentSuccessful(UUID orderId) {
        log.info("Processing payment success for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        // Update order status to CONFIRMED
        order.markAsConfirmed();
        Order confirmedOrder = orderRepository.save(order);

        // Publish order.confirmed event (Step 11 - for notifications)
        orderEventPublisher.publishOrderConfirmed(confirmedOrder);

        log.info("Order {} confirmed, published order.confirmed event", orderId);
    }

    @Override
    @Transactional
    public void handlePaymentFailed(UUID orderId) {
        log.warn("Processing payment failure for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        // Update order status to CANCELLED
        order.markAsCancelled();
        Order cancelledOrder = orderRepository.save(order);

        // Publish order.cancelled event (Step 11 - triggers compensation in Flight Service)
        orderEventPublisher.publishOrderCancelled(cancelledOrder);

        log.info("Order {} cancelled, published order.cancelled event for compensation", orderId);
    }
}

