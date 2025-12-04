package org.example.order.infrastructure.persistence.repository;

import org.example.order.domain.entity.OrderStatus;
import org.example.order.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA Repository
 */
@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
    List<OrderJpaEntity> findByCustomerId(UUID customerId);
    
    /**
     * Find orders with PENDING_PAYMENT status that have expired
     * @param status Order status (should be PENDING_PAYMENT)
     * @param expiryTime Current time to compare against
     * @return List of expired orders
     */
    List<OrderJpaEntity> findByStatusAndReservationExpiresAtBefore(OrderStatus status, LocalDateTime expiryTime);
}

