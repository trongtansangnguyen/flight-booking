package org.example.payment.infrastructure.persistence.repository;

import org.example.payment.infrastructure.persistence.entity.CustomerCreditJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerCreditSpringDataRepository extends JpaRepository<CustomerCreditJpaEntity, UUID> {
    Optional<CustomerCreditJpaEntity> findByCustomerId(UUID customerId);
}