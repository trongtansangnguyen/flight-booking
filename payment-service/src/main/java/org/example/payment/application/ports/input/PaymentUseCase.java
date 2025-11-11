package org.example.payment.application.ports.input;

import org.example.payment.application.dto.ProcessPaymentCommand;
import org.example.payment.application.dto.RefundPaymentCommand;

public interface PaymentUseCase {
    // Luồng SAGA chính
    void processPayment(ProcessPaymentCommand command);

    // Luồng SAGA compensation
    void processRefund(RefundPaymentCommand command);
}