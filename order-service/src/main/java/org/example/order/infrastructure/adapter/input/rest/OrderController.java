package org.example.order.infrastructure.adapter.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order.application.dto.CreateOrderRequest;
import org.example.order.application.dto.OrderResponse;
import org.example.order.application.ports.input.CreateOrderUseCase;
import org.example.order.application.ports.input.GetOrderUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller - Infrastructure Layer (Input Adapter)
 * Handles HTTP requests for order operations
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    /**
     * Create a new order
     * Step 1-3 in Saga workflow
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        OrderResponse response = createOrderUseCase.createOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID orderId) {
        OrderResponse response = getOrderUseCase.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all orders for a customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomerId(@PathVariable UUID customerId) {
        List<OrderResponse> responses = getOrderUseCase.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all orders
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> responses = getOrderUseCase.getAllOrders();
        return ResponseEntity.ok(responses);
    }
}

