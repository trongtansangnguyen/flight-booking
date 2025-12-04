package org.example.payment.infrastructure.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.payment.presentation.messaging.dto.OrderCreatedEvent;
import org.example.payment.presentation.messaging.dto.OrderRefundRequestedEvent;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Dedicated Kafka consumer configuration for Payment Service.
 * Similar approach to Order Service: each incoming event type gets
 * its own ConsumerFactory + ContainerFactory so JsonDeserializer
 * knows the exact target class without relying on headers.
 */
@EnableKafka
@Configuration
public class PaymentKafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public PaymentKafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    private Map<String, Object> baseConsumerProps() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    // === OrderCreatedEvent ===

    @Bean
    public ConsumerFactory<String, OrderCreatedEvent> orderCreatedConsumerFactory() {
        JsonDeserializer<OrderCreatedEvent> valueDeserializer =
                new JsonDeserializer<>(OrderCreatedEvent.class, false);
        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(),
                new StringDeserializer(),
                valueDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent>
    orderCreatedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderCreatedConsumerFactory());
        return factory;
    }

    // === OrderRefundRequestedEvent ===

    @Bean
    public ConsumerFactory<String, OrderRefundRequestedEvent> orderRefundRequestedConsumerFactory() {
        JsonDeserializer<OrderRefundRequestedEvent> valueDeserializer =
                new JsonDeserializer<>(OrderRefundRequestedEvent.class, false);
        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(),
                new StringDeserializer(),
                valueDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderRefundRequestedEvent>
    orderRefundRequestedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderRefundRequestedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderRefundRequestedConsumerFactory());
        return factory;
    }
}


