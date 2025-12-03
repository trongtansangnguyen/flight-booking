package org.example.order.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.dto.OrderResponse;
import org.example.order.application.mapper.OrderMapper;
import org.example.order.application.ports.input.GetOrderUseCase;
import org.example.order.application.ports.output.OrderRepository;
import org.example.order.domain.entity.Order;
import org.example.order.domain.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case Implementation for querying orders
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetOrderUseCaseImpl implements GetOrderUseCase {
    
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    
    @Override
    public OrderResponse getOrderById(UUID orderId) {
        log.info("Getting order by ID: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        
        return orderMapper.toResponse(order);
    }
    
    @Override
    public List<OrderResponse> getOrdersByCustomerId(UUID customerId) {
        log.info("Getting orders for customer: {}", customerId);
        
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderResponse> getAllOrders() {
        log.info("Getting all orders");
        
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }
}

