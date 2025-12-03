package org.example.order.infrastructure.persistence.mapper;

import org.example.order.domain.entity.Order;
import org.example.order.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Order domain entity and OrderJpaEntity
 * Infrastructure Layer - Persistence mapping
 */
@Component
public class OrderPersistenceMapper {

    /**
     * Maps Domain Entity to JPA Entity
     */
    public OrderJpaEntity toJpaEntity(Order order) {
        if (order == null) {
            return null;
        }

        return OrderJpaEntity.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .flightId(order.getFlightId())
                .quantityOfTickets(order.getQuantityOfTickets())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .failureReason(order.getFailureReason())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    /**
     * Maps JPA Entity to Domain Entity
     */
    public Order toDomainEntity(OrderJpaEntity jpa) {
        if (jpa == null) {
            return null;
        }

        Order order = new Order();
        order.setId(jpa.getId());
        order.setCustomerId(jpa.getCustomerId());
        order.setFlightId(jpa.getFlightId());
        order.setQuantityOfTickets(jpa.getQuantityOfTickets());
        order.setTotalPrice(jpa.getTotalPrice());
        order.setStatus(jpa.getStatus());
        order.setFailureReason(jpa.getFailureReason());
        order.setCreatedAt(jpa.getCreatedAt());
        order.setUpdatedAt(jpa.getUpdatedAt());
        return order;
    }
}

