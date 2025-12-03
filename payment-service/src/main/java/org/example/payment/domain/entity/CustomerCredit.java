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

    /**
     * Debit (subtract) amount from customer credit
     * @param amount amount to debit (must not be null)
     * @throws PaymentDomainException if amount is null or would exceed credit limit
     */
    public void debit(Money amount) {
        validateAmount(amount);
        validateState();
        
        // Kiểm tra không được trừ nhiều hơn số dư hiện tại
        if (amount.isGreaterThan(this.currentBalance)) {
            throw new PaymentDomainException(
                    String.format("Insufficient credit balance for customer: %s. " +
                            "Current balance: %s, Requested amount: %s",
                            customerId.value(),
                            currentBalance.amount(),
                            amount.amount()));
        }
        // Thực sự trừ tiền
        this.currentBalance = this.currentBalance.subtract(amount);
    }

    /**
     * Credit (add) amount back to customer credit (for refunds)
     * @param amount amount to credit (must not be null)
     * @throws PaymentDomainException if amount is null
     */
    public void credit(Money amount) {
        validateAmount(amount);
        validateState();
        
        // Kiểm tra refund không được vượt quá credit limit
        Money newBalance = this.currentBalance.add(amount);
        if (newBalance.isGreaterThan(this.creditLimit)) {
            throw new PaymentDomainException(
                    String.format("Refund would exceed credit limit for customer: %s. " +
                            "Current balance: %s, Credit limit: %s, Refund amount: %s",
                            customerId.value(),
                            currentBalance.amount(),
                            creditLimit.amount(),
                            amount.amount()));
        }
        // Thực sự hoàn tiền (cộng vào balance)
        this.currentBalance = newBalance;
    }

    private void validateAmount(Money amount) {
        if (amount == null) {
            throw new PaymentDomainException("Amount cannot be null for customer: " + 
                    (customerId != null ? customerId.value() : "unknown"));
        }
    }

    private void validateState() {
        if (this.customerId == null) {
            throw new PaymentDomainException("Customer ID cannot be null");
        }
        if (this.creditLimit == null) {
            throw new PaymentDomainException("Credit limit cannot be null for customer: " + customerId.value());
        }
        if (this.currentBalance == null) {
            throw new PaymentDomainException("Current balance cannot be null for customer: " + customerId.value());
        }
    }
}