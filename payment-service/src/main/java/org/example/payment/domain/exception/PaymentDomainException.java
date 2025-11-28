package org.example.payment.domain.exception;

// Exception nghiệp vụ thuần túy
public class PaymentDomainException extends RuntimeException {
    public PaymentDomainException(String message) {
        super(message);
    }
}