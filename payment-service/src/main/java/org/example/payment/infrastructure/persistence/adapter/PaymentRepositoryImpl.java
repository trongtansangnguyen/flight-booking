package org.example.payment.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.ports.output.PaymentRepository;
import org.example.payment.domain.entity.Payment;
import org.example.payment.domain.valueobject.OrderId;
import org.example.payment.infrastructure.persistence.mapper.PaymentPersistenceMapper;
import org.example.payment.infrastructure.persistence.repository.PaymentSpringDataRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentSpringDataRepository jpaRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public Payment save(Payment payment) {
        var jpaEntity = mapper.domainToJpaEntity(payment);
        var savedEntity = jpaRepository.save(jpaEntity);
        return mapper.jpaEntityToDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findByOrderId(OrderId orderId) {
        return jpaRepository.findByOrderId(orderId.value())
                .map(mapper::jpaEntityToDomain);
    }
}