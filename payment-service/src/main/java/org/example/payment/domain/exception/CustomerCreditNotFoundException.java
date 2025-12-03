package org.example.payment.domain.exception;

/**
 * Domain Exception - Customer Credit not found
 */
public class CustomerCreditNotFoundException extends RuntimeException {
    public CustomerCreditNotFoundException(String message) {
        super(message);
    }

    public CustomerCreditNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

