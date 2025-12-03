package org.example.payment.application.ports.input;

import org.example.payment.application.dto.CustomerCreditResponse;

import java.util.UUID;

public interface GetCustomerCreditUseCase {
    CustomerCreditResponse getCustomerCredit(UUID customerId);
}

