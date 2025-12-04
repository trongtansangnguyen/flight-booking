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

        // Option B: Manual Retry - Keep order in PENDING_PAYMENT status
        // User can retry payment via API endpoint
        // Order will only be cancelled if:
        // 1. Reservation expires (handled by scheduled job)
        // 2. User manually cancels
        
        // Check if reservation has expired
        if (order.isReservationExpired()) {
            // Reservation expired, cancel order and release seats
            order.markAsCancelled("Payment failed and reservation expired");
            Order cancelledOrder = orderRepository.save(order);
            orderEventPublisher.publishOrderCancelled(cancelledOrder);
            log.info("Order {} cancelled due to payment failure and expired reservation. Published order.cancelled event.", orderId);
        } else {
            // Reservation still valid, keep order in PENDING_PAYMENT for manual retry
            // Just log the payment failure, order remains in PENDING_PAYMENT
            log.warn("Payment failed for order: {}. Order remains in PENDING_PAYMENT status. " +
                    "User can retry payment via POST /api/orders/{}/retry-payment", orderId, orderId);
        }
    }
}

