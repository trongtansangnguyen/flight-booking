package org.example.payment.presentation.messaging.mapper;

import org.example.payment.application.dto.ProcessPaymentCommand;
import org.example.payment.application.dto.RefundPaymentCommand;
import org.example.payment.presentation.messaging.dto.OrderCreatedEvent;
import org.example.payment.presentation.messaging.dto.OrderRefundRequestedEvent;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert incoming Kafka events to application commands
 * Manual implementation to avoid MapStruct issues with record types
 */
@Component
public class PaymentEventMapper {

    /**
     * Maps OrderCreatedEvent to ProcessPaymentCommand
     * @param event incoming order created event
     * @return process payment command
     */
    public ProcessPaymentCommand orderCreatedEventToProcessCommand(OrderCreatedEvent event) {
        if (event == null) {
            return null;
        }
        
        // Record types use constructor directly, not builder pattern
        return new ProcessPaymentCommand(
                event.orderId(),
                event.customerId(),
                event.totalPrice()
        );
    }

    /**
     * Maps OrderRefundRequestedEvent to RefundPaymentCommand
     * @param event incoming order refund requested event
     * @return refund payment command
     */
    public RefundPaymentCommand orderRefundRequestedEventToRefundCommand(OrderRefundRequestedEvent event) {
        if (event == null) {
            return null;
        }
        
        // Record types use constructor directly, not builder pattern
        return new RefundPaymentCommand(event.orderId());
    }
}