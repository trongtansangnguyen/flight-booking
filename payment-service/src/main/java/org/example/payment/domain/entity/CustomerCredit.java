package org.example.payment.domain.entity;

import org.example.payment.domain.exception.PaymentDomainException;
import org.example.payment.domain.valueobject.CustomerId;
import org.example.payment.domain.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomerCredit {
    private final CustomerId customerId;
    private Money creditLimit;
    private Money currentBalance;

    public void debit(Money amount) {
        if (this.currentBalance.add(amount).isGreaterThan(this.creditLimit)) {
            throw new PaymentDomainException("Insufficient credit balance for customer: " + customerId.value());
        }
        this.currentBalance = this.currentBalance.add(amount);
    }

    public void credit(Money amount) {
        this.currentBalance = this.currentBalance.subtract(amount);
    }
}