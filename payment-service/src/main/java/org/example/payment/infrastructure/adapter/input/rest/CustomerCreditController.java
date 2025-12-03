package org.example.payment.infrastructure.adapter.input.rest;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.dto.CustomerCreditResponse;
import org.example.payment.application.ports.input.GetCustomerCreditUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller - Infrastructure Layer (Input Adapter)
 * Handles HTTP requests for customer credit operations
 */
@RestController
@RequestMapping("/api/customer-credits")
@RequiredArgsConstructor
public class CustomerCreditController {

    private final GetCustomerCreditUseCase getCustomerCreditUseCase;

    /**
     * Get customer credit information by customer ID
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerCreditResponse> getCustomerCredit(@PathVariable UUID customerId) {
        CustomerCreditResponse response = getCustomerCreditUseCase.getCustomerCredit(customerId);
        return ResponseEntity.ok(response);
    }
}

