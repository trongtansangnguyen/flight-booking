package org.example.order.infrastructure.adapter.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order.application.dto.CancelOrderRequest;
import org.example.order.application.dto.CreateOrderRequest;
import org.example.order.application.dto.OrderResponse;
import org.example.order.application.ports.input.CancelOrderUseCase;
import org.example.order.application.ports.input.CreateOrderUseCase;
import org.example.order.application.ports.input.GetOrderUseCase;
import org.example.order.application.ports.input.RetryPaymentUseCase;
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
    private final RetryPaymentUseCase retryPaymentUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

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

    /**
     * Retry payment for an order (Manual retry - Option B)
     * Only works if order is in PENDING_PAYMENT status and not expired
     * 
     * @param orderId Order ID to retry payment for
     * @return 200 OK if retry initiated successfully
     */
    @PostMapping("/{orderId}/retry-payment")
    public ResponseEntity<Void> retryPayment(@PathVariable UUID orderId) {
        retryPaymentUseCase.retryPayment(orderId);
        return ResponseEntity.ok().build();
    }

    /**
     * Cancel an order (Customer-initiated)
     * Validates:
     * - Order must be in PENDING_PAYMENT or CONFIRMED status
     * - Cancellation must be at least 24 hours before flight departure
     * - Customer must own the order
     * 
     * If payment was completed, triggers refund
     * Publishes order.cancelled event to release seats
     * 
     * @param orderId Order ID to cancel
     * @param request Cancel order request containing customerId
     * @return 200 OK if cancellation successful
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable UUID orderId,
            @RequestBody @Valid CancelOrderRequest request) {
        cancelOrderUseCase.cancelOrder(orderId, request.customerId());
        return ResponseEntity.ok().build();
    }
}

