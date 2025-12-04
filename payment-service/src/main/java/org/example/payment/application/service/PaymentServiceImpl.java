package org.example.payment.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.payment.application.dto.ProcessPaymentCommand;
import org.example.payment.application.dto.RefundPaymentCommand;
import org.example.payment.application.ports.input.PaymentUseCase;
import org.example.payment.application.ports.output.CustomerCreditRepository;
import org.example.payment.application.ports.output.PaymentMessagePublisher;
import org.example.payment.application.ports.output.PaymentRepository;
import org.example.payment.domain.entity.CustomerCredit;
import org.example.payment.domain.entity.Payment;
import org.example.payment.domain.exception.PaymentDomainException;
import org.example.payment.domain.valueobject.CustomerId;
import org.example.payment.domain.valueobject.Money;
import org.example.payment.domain.valueobject.OrderId;
import org.example.payment.domain.valueobject.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor // Tự động inject các Port qua constructor
public class PaymentServiceImpl implements PaymentUseCase {

    private final CustomerCreditRepository customerCreditRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMessagePublisher paymentMessagePublisher;

    @Override
    @Transactional
    public void processPayment(ProcessPaymentCommand command) {
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(new OrderId(command.orderId()));

        Payment payment;
        
        if (existingPayment.isPresent()) {
            Payment existing = existingPayment.get();
            
            // Idempotent handling: Nếu payment đã COMPLETED, chỉ republish event
            if (existing.getStatus() == PaymentStatus.COMPLETED) {
                paymentMessagePublisher.publishPaymentCompleted(existing);
                log.warn("Payment for order: {} already COMPLETED. Republishing event.", command.orderId());
                return;
            }
            
            // Retry logic: Nếu payment đã FAILED, reset và retry lại
            if (existing.getStatus() == PaymentStatus.FAILED) {
                log.info("Payment for order: {} was FAILED. Resetting for retry.", command.orderId());
                existing.resetForRetry();
                payment = existing;
            } else {
                // Payment đang ở status khác (PENDING), republish event
                if (existing.getStatus() == PaymentStatus.PENDING) {
                    log.warn("Payment for order: {} is already PENDING. Republishing payment.failed event.", command.orderId());
                    paymentMessagePublisher.publishPaymentFailed(existing);
                } else {
                    log.warn("Payment for order: {} has unexpected status: {}. Republishing payment.failed event.", 
                            command.orderId(), existing.getStatus());
                    paymentMessagePublisher.publishPaymentFailed(existing);
                }
                return;
            }
        } else {
            // Tạo Payment entity mới
            log.info("Processing new payment for order: {}", command.orderId());
            payment = Payment.builder()
                    .orderId(new OrderId(command.orderId()))
                    .customerId(new CustomerId(command.customerId()))
                    .amount(new Money(command.amount()))
                    .build();
            payment.initializePayment();
        }

        // Thực hiện payment (mới hoặc retry)
        try {
            // Lấy Domain Entity CustomerCredit
            CustomerCredit customerCredit = customerCreditRepository
                    .findByCustomerId(payment.getCustomerId())
                    .orElseThrow(() -> new PaymentDomainException("Customer not found"));

            // Gọi Domain Logic (trừ tiền)
            customerCredit.debit(payment.getAmount());
            payment.complete(); // Đánh dấu payment thành công

            // Lưu trạng thái mới
            customerCreditRepository.save(customerCredit);
            Payment savedPayment = paymentRepository.save(payment);

            // Publish sự kiện SAGA (Happy Path)
            paymentMessagePublisher.publishPaymentCompleted(savedPayment);
            log.info("Payment COMPLETED for order: {}", savedPayment.getOrderId().value());

        } catch (PaymentDomainException e) {
            log.warn("Payment FAILED for order: {}. Reason: {}", command.orderId(), e.getMessage());
            payment.fail(e.getMessage());
            paymentRepository.save(payment);

            // Publish sự kiện SAGA (Failure Path)
            paymentMessagePublisher.publishPaymentFailed(payment);
        }
    }

    @Override
    @Transactional
    public void processRefund(RefundPaymentCommand command) {
        log.info("Processing refund (SAGA Compensation) for order: {}", command.orderId());
        try {
            Payment payment = paymentRepository
                    .findByOrderId(new OrderId(command.orderId()))
                    .orElseThrow(() -> new PaymentDomainException("Payment not found for order to refund"));

            CustomerCredit customerCredit = customerCreditRepository
                    .findByCustomerId(payment.getCustomerId())
                    .orElseThrow(() -> new PaymentDomainException("Customer not found for refund"));

            // Gọi Domain Logic (hoàn tiền)
            customerCredit.credit(payment.getAmount());
            payment.refund();

            // Lưu trạng thái
            customerCreditRepository.save(customerCredit);
            Payment refundedPayment = paymentRepository.save(payment);

            // Publish sự kiện SAGA (Compensation)
            paymentMessagePublisher.publishPaymentRefunded(refundedPayment);
            log.info("Payment REFUNDED for order: {}", refundedPayment.getOrderId().value());

        } catch (PaymentDomainException e) {
            log.error("Refund FAILED for order: {}. Reason: {}", command.orderId(), e.getMessage());
            
            // Note: Compensation failure is a critical situation
            // The refund failed, which means we cannot reverse the payment
            // This should be monitored and may require manual intervention
            // In production, consider:
            // 1. Publishing a refund-failed event for monitoring
            // 2. Sending alerts to operations team
            // 3. Logging to a separate error tracking system
            
            // We don't throw the exception here to avoid transaction rollback issues
            // The error is logged for monitoring and manual intervention
        }
    }
}