package org.example.order.infrastructure.config;

import org.example.order.application.mapper.OrderMapper;
import org.example.order.application.ports.input.CreateOrderUseCase;
import org.example.order.application.ports.input.GetOrderUseCase;
import org.example.order.application.ports.input.ProcessPaymentResultUseCase;
import org.example.order.application.ports.input.ProcessSeatReservationResultUseCase;
import org.example.order.application.ports.output.OrderEventPublisher;
import org.example.order.application.ports.output.OrderRepository;
import org.example.order.application.usecase.CreateOrderUseCaseImpl;
import org.example.order.application.usecase.GetOrderUseCaseImpl;
import org.example.order.application.usecase.ProcessPaymentResultUseCaseImpl;
import org.example.order.application.usecase.ProcessSeatReservationResultUseCaseImpl;
import org.example.order.infrastructure.persistence.adapter.OrderRepositoryAdapter;
import org.example.order.infrastructure.persistence.mapper.OrderPersistenceMapper;
import org.example.order.infrastructure.persistence.repository.OrderJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration - Infrastructure Layer (Container Layer)
 * Wiring all dependencies following Clean Architecture
 */
@Configuration
public class OrderBeanConfiguration {

    /**
     * Bean for Repository Adapter (Output Adapter)
     */
    @Bean
    public OrderRepository orderRepository(OrderJpaRepository jpaRepository, OrderPersistenceMapper mapper) {
        return new OrderRepositoryAdapter(jpaRepository, mapper);
    }

    /**
     * Bean for CreateOrderUseCase
     */
    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepository orderRepository,
                                                  OrderEventPublisher orderEventPublisher,
                                                  OrderMapper orderMapper) {
        return new CreateOrderUseCaseImpl(orderRepository, orderEventPublisher, orderMapper);
    }

    /**
     * Bean for ProcessSeatReservationResultUseCase
     */
    @Bean
    public ProcessSeatReservationResultUseCase processSeatReservationResultUseCase(
            OrderRepository orderRepository,
            OrderEventPublisher orderEventPublisher) {
        return new ProcessSeatReservationResultUseCaseImpl(orderRepository, orderEventPublisher);
    }

    /**
     * Bean for ProcessPaymentResultUseCase
     */
    @Bean
    public ProcessPaymentResultUseCase processPaymentResultUseCase(
            OrderRepository orderRepository,
            OrderEventPublisher orderEventPublisher) {
        return new ProcessPaymentResultUseCaseImpl(orderRepository, orderEventPublisher);
    }

    /**
     * Bean for GetOrderUseCase
     */
    @Bean
    public GetOrderUseCase getOrderUseCase(OrderRepository orderRepository, OrderMapper orderMapper) {
        return new GetOrderUseCaseImpl(orderRepository, orderMapper);
    }
}

