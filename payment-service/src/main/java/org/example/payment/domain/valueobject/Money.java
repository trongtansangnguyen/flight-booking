package org.example.payment.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public record Money(BigDecimal amount) {
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        Objects.requireNonNull(amount);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public Money add(Money moneyToAdd) {
        return new Money(this.amount.add(moneyToAdd.amount()));
    }

    public Money subtract(Money moneyToSubtract) {
        if (this.isLessThan(moneyToSubtract)) {
            throw new IllegalArgumentException("Insufficient funds for subtraction");
        }
        return new Money(this.amount.subtract(moneyToSubtract.amount()));
    }

    public boolean isGreaterThan(Money other) {
        return this.amount.compareTo(other.amount()) > 0;
    }

    public boolean isLessThan(Money other) {
        return this.amount.compareTo(other.amount()) < 0;
    }
}