package org.example.order.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.ports.input.CancelOrderUseCase;
import org.example.order.application.ports.output.FlightServicePort;
import org.example.order.application.ports.output.OrderEventPublisher;
import org.example.order.application.ports.output.OrderRepository;
import org.example.order.domain.entity.Order;
import org.example.order.domain.entity.OrderStatus;
import org.example.order.domain.exception.OrderDomainException;
import org.example.order.domain.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Use Case Implementation for cancelling an order
 * Application Layer - Business Logic
 * 
 * Validates cancellation rules and triggers refund if needed
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CancelOrderUseCaseImpl implements CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final FlightServicePort flightServicePort;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    @Transactional
    public void cancelOrder(UUID orderId, UUID customerId) {
        log.info("Customer {} requesting to cancel order: {}", customerId, orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));
        
        // Validate customer owns the order
        if (!order.getCustomerId().equals(customerId)) {
            throw new OrderDomainException(
                    String.format("Customer %s is not authorized to cancel order %s", customerId, orderId));
        }
        
        // Validate order status
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT && 
            order.getStatus() != OrderStatus.CONFIRMED) {
            throw new OrderDomainException(
                    String.format("Cannot cancel order. Order must be in PENDING_PAYMENT or CONFIRMED status. " +
                            "Current status: %s", order.getStatus()));
        }
        
        // Get flight departure time
        LocalDateTime departureTime = flightServicePort.getFlightDepartureTime(order.getFlightId())
                .orElseThrow(() -> new OrderDomainException(
                        String.format("Cannot get flight departure time for flight: %s", order.getFlightId())));
        
        // Validate cancellation time (at least 24 hours before departure)
        if (!order.canBeCancelled(departureTime)) {
            LocalDateTime minCancelTime = departureTime.minusHours(24);
            throw new OrderDomainException(
                    String.format("Cannot cancel order. Cancellation must be at least 24 hours before departure. " +
                            "Flight departs at: %s, Minimum cancel time: %s, Current time: %s",
                            departureTime, minCancelTime, LocalDateTime.now()));
        }
        
        // Check if payment was completed (order is CONFIRMED)
        boolean paymentCompleted = order.getStatus() == OrderStatus.CONFIRMED;
        
        // Cancel the order
        order.markAsCancelled("Cancelled by customer");
        Order cancelledOrder = orderRepository.save(order);
        
        // Publish order.cancelled event to release seats
        orderEventPublisher.publishOrderCancelled(cancelledOrder);
        
        // If payment was completed, trigger refund
        if (paymentCompleted) {
            log.info("Order {} was CONFIRMED. Triggering refund for customer: {}", orderId, customerId);
            orderEventPublisher.publishOrderRefundRequested(orderId, customerId, order.getTotalPrice());
        }
        
        log.info("Order {} cancelled successfully by customer: {}", orderId, customerId);
    }
}

