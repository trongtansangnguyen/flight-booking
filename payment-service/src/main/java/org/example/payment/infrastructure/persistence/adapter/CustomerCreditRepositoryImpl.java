package org.example.payment.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.ports.output.CustomerCreditRepository;
import org.example.payment.domain.entity.CustomerCredit;
import org.example.payment.domain.valueobject.CustomerId;
import org.example.payment.infrastructure.persistence.entity.CustomerCreditJpaEntity;
import org.example.payment.infrastructure.persistence.mapper.CustomerCreditPersistenceMapper;
import org.example.payment.infrastructure.persistence.repository.CustomerCreditSpringDataRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerCreditRepositoryImpl implements CustomerCreditRepository {

    private final CustomerCreditSpringDataRepository jpaRepository;
    private final CustomerCreditPersistenceMapper mapper; // MapStruct Mapper

    @Override
    public Optional<CustomerCredit> findByCustomerId(CustomerId customerId) {
        return jpaRepository.findByCustomerId(customerId.value())
                .map(mapper::jpaEntityToDomain);
    }

    @Override
    public CustomerCredit save(CustomerCredit customerCredit) {
        UUID customerIdValue = customerCredit.getCustomerId().value();
        CustomerCreditJpaEntity jpaEntity = jpaRepository.findByCustomerId(customerIdValue)
                .orElseThrow(() -> new IllegalStateException("Customer credit not found, cannot update. " +
                        "Payment processing logic should have caught this."));
        mapper.updateJpaEntityFromDomain(customerCredit, jpaEntity);
        var savedEntity = jpaRepository.save(jpaEntity);
        return mapper.jpaEntityToDomain(savedEntity);
    }
}