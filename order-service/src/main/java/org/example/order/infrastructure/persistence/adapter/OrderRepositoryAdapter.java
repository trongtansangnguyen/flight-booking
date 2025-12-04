package org.example.order.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.order.application.ports.output.OrderRepository;
import org.example.order.domain.entity.Order;
import org.example.order.domain.entity.OrderStatus;
import org.example.order.infrastructure.persistence.mapper.OrderPersistenceMapper;
import org.example.order.infrastructure.persistence.repository.OrderJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Repository Adapter - Infrastructure Layer
 * Implements OrderRepository port from Application Layer
 */
@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public Order save(Order order) {
        var jpaEntity = mapper.toJpaEntity(order);
        var savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return jpaRepository.findById(orderId)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Order> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatusAndReservationExpiresAtBefore(OrderStatus status, LocalDateTime expiryTime) {
        return jpaRepository.findByStatusAndReservationExpiresAtBefore(status, expiryTime).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }
}

