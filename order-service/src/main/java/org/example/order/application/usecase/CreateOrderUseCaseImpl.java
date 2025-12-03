package org.example.order.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.dto.CreateOrderRequest;
import org.example.order.application.dto.OrderResponse;
import org.example.order.application.mapper.OrderMapper;
import org.example.order.application.ports.input.CreateOrderUseCase;
import org.example.order.application.ports.output.OrderEventPublisher;
import org.example.order.application.ports.output.OrderRepository;
import org.example.order.domain.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case Implementation for creating a new order
 * Step 1-3 in the Saga workflow:
 * 1. User requests booking
 * 2. Create order with RESERVING status
 * 3. Publish order.reservation.requested event
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for customer: {}, flight: {}, quantity: {}", 
                request.customerId(), request.flightId(), request.quantityOfTickets());

        // Create domain entity with RESERVING status
        Order order = Order.create(
                request.customerId(),
                request.flightId(),
                request.quantityOfTickets(),
                request.totalPrice()
        );

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Publish event to request seat reservation (Step 3)
        orderEventPublisher.publishOrderReservationRequested(
                savedOrder.getId(),
                savedOrder.getFlightId(),
                savedOrder.getQuantityOfTickets()
        );

        log.info("Order created with ID: {}, status: {}", savedOrder.getId(), savedOrder.getStatus());
        
        return orderMapper.toResponse(savedOrder);
    }
}

