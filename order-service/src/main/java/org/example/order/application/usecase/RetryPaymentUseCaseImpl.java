package org.example.order.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.ports.input.RetryPaymentUseCase;
import org.example.order.application.ports.output.OrderEventPublisher;
import org.example.order.application.ports.output.OrderRepository;
import org.example.order.domain.entity.Order;
import org.example.order.domain.entity.OrderStatus;
import org.example.order.domain.exception.OrderDomainException;
import org.example.order.domain.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case Implementation for manual payment retry
 * Application Layer - Business Logic
 * 
 * Allows user to manually retry payment for an order that is in PENDING_PAYMENT status
 * and has not expired. Publishes order.created event again to trigger payment service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RetryPaymentUseCaseImpl implements RetryPaymentUseCase {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    @Transactional
    public void retryPayment(UUID orderId) {
        log.info("Manual payment retry requested for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));
        
        // Validate order status
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new OrderDomainException(
                    String.format("Cannot retry payment. Order must be in PENDING_PAYMENT status. Current status: %s", 
                            order.getStatus()));
        }
        
        // Check if reservation has expired
        if (order.isReservationExpired()) {
            throw new OrderDomainException(
                    "Cannot retry payment. Reservation has expired. Please create a new order.");
        }
        
        // Publish order.created event again to trigger payment service retry
        orderEventPublisher.publishOrderCreated(
                order.getId(),
                order.getCustomerId(),
                order.getTotalPrice()
        );
        
        log.info("Payment retry initiated for order: {}. Published order.created event.", orderId);
    }
}

