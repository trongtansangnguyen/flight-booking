package org.example.payment.presentation.messaging.mapper;

import org.example.payment.application.dto.ProcessPaymentCommand;
import org.example.payment.application.dto.RefundPaymentCommand;
import org.example.payment.presentation.messaging.dto.FlightReservationFailedEvent;
import org.example.payment.presentation.messaging.dto.OrderCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentEventMapper {

    // MapStruct tự biết "totalAmount" -> "amount"
    @Mapping(source = "totalAmount", target = "amount")
    ProcessPaymentCommand orderCreatedEventToProcessCommand(OrderCreatedEvent event);

    // Map sự kiện bù trừ
    RefundPaymentCommand flightFailedEventToRefundCommand(FlightReservationFailedEvent event);

}