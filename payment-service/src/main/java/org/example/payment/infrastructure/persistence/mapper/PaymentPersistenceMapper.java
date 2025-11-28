package org.example.payment.infrastructure.persistence.mapper;

import org.example.payment.domain.entity.Payment;
import org.example.payment.domain.valueobject.CustomerId;
import org.example.payment.domain.valueobject.Money;
import org.example.payment.domain.valueobject.OrderId;
import org.example.payment.domain.valueobject.PaymentId;
import org.example.payment.infrastructure.persistence.entity.PaymentJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PaymentPersistenceMapper {

    // MapStruct tự động map các trường cùng tên (status, customerId, orderId, amount)
    // Nhưng chúng ta cần "chỉ" nó cách map các Value Object

    @Mapping(source = "id", target = "paymentId")
    Payment jpaEntityToDomain(PaymentJpaEntity entity);

    @Mapping(source = "paymentId", target = "id")
    PaymentJpaEntity domainToJpaEntity(Payment domain);

    // === Các phương thức trợ giúp Map Value Objects ===
    default PaymentId map(UUID value) { return (value == null) ? null : new PaymentId(value); }
    default UUID map(PaymentId value) { return (value == null) ? null : value.value(); }

    default CustomerId mapCustomerId(UUID value) { return (value == null) ? null : new CustomerId(value); }
    default UUID map(CustomerId value) { return (value == null) ? null : value.value(); }

    default OrderId mapOrderId(UUID value) { return (value == null) ? null : new OrderId(value); }
    default UUID map(OrderId value) { return (value == null) ? null : value.value(); }

    default Money map(BigDecimal value) { return (value == null) ? null : new Money(value); }
    default BigDecimal map(Money value) { return (value == null) ? null : value.amount(); }
}