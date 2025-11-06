package org.example.payment.application.ports.output;

import org.example.payment.domain.entity.CustomerCredit;
import org.example.payment.domain.valueobject.CustomerId;

import java.util.Optional;

// Output Port
public interface CustomerCreditRepository {
    Optional<CustomerCredit> findByCustomerId(CustomerId customerId);
    CustomerCredit save(CustomerCredit customerCredit);
}