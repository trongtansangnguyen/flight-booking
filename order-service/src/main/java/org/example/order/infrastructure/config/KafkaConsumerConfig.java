package org.example.order.infrastructure.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.order.infrastructure.messaging.dto.PaymentFailedEvent;
import org.example.order.infrastructure.messaging.dto.PaymentSuccessfulEvent;
import org.example.order.infrastructure.messaging.dto.SeatReservationFailedEvent;
import org.example.order.infrastructure.messaging.dto.SeatReservedEvent;
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
 * Dedicated Kafka consumer configuration for Order Service.
 * <p>
 * Each event type has its own ConsumerFactory + ContainerFactory so that
 * JsonDeserializer knows exactly which target class to deserialize to,
 * without relying on Kafka type headers or default types from YAML.
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    private Map<String, Object> baseConsumerProps() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        // Ensure key deserializer is String
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    // === SeatReservedEvent ===

    @Bean
    public ConsumerFactory<String, SeatReservedEvent> seatReservedConsumerFactory() {
        JsonDeserializer<SeatReservedEvent> valueDeserializer =
                new JsonDeserializer<>(SeatReservedEvent.class, false);
        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(),
                new StringDeserializer(),
                valueDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SeatReservedEvent>
    seatReservedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SeatReservedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(seatReservedConsumerFactory());
        return factory;
    }

    // === SeatReservationFailedEvent ===

    @Bean
    public ConsumerFactory<String, SeatReservationFailedEvent> seatReservationFailedConsumerFactory() {
        JsonDeserializer<SeatReservationFailedEvent> valueDeserializer =
                new JsonDeserializer<>(SeatReservationFailedEvent.class, false);
        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(),
                new StringDeserializer(),
                valueDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SeatReservationFailedEvent>
    seatReservationFailedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SeatReservationFailedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(seatReservationFailedConsumerFactory());
        return factory;
    }

    // === PaymentSuccessfulEvent ===

    @Bean
    public ConsumerFactory<String, PaymentSuccessfulEvent> paymentSuccessfulConsumerFactory() {
        JsonDeserializer<PaymentSuccessfulEvent> valueDeserializer =
                new JsonDeserializer<>(PaymentSuccessfulEvent.class, false);
        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(),
                new StringDeserializer(),
                valueDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentSuccessfulEvent>
    paymentSuccessfulKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentSuccessfulEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentSuccessfulConsumerFactory());
        return factory;
    }

    // === PaymentFailedEvent ===

    @Bean
    public ConsumerFactory<String, PaymentFailedEvent> paymentFailedConsumerFactory() {
        JsonDeserializer<PaymentFailedEvent> valueDeserializer =
                new JsonDeserializer<>(PaymentFailedEvent.class, false);
        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(),
                new StringDeserializer(),
                valueDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentFailedEvent>
    paymentFailedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentFailedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentFailedConsumerFactory());
        return factory;
    }
}


