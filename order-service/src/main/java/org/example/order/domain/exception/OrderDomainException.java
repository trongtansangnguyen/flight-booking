package org.example.order.domain.exception;

/**
 * Domain Exception for business rule violations
 */
public class OrderDomainException extends RuntimeException {
    public OrderDomainException(String message) {
        super(message);
    }

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

