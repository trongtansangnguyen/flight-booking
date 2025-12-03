package org.example.payment.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.payment.application.dto.CustomerCreditResponse;
import org.example.payment.application.ports.input.GetCustomerCreditUseCase;
import org.example.payment.application.ports.output.CustomerCreditRepository;
import org.example.payment.domain.entity.CustomerCredit;
import org.example.payment.domain.exception.CustomerCreditNotFoundException;
import org.example.payment.domain.valueobject.CustomerId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetCustomerCreditServiceImpl implements GetCustomerCreditUseCase {

    private final CustomerCreditRepository customerCreditRepository;

    @Override
    public CustomerCreditResponse getCustomerCredit(UUID customerId) {
        log.info("Getting customer credit for customer: {}", customerId);
        
        CustomerCredit customerCredit = customerCreditRepository
                .findByCustomerId(new CustomerId(customerId))
                .orElseThrow(() -> new CustomerCreditNotFoundException(
                        "Customer credit not found for customer: " + customerId));

        return new CustomerCreditResponse(
                customerCredit.getCustomerId().value(),
                customerCredit.getCreditLimit().amount(),
                customerCredit.getCurrentBalance().amount()
        );
    }
}

