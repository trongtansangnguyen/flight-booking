package org.example.flight.infrastructure.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.flight.infrastructure.adapter.output.messaging.dto.OrderCancelledEvent;
import org.example.flight.infrastructure.adapter.output.messaging.dto.OrderReservationRequestedEvent;
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

@Configuration
@EnableKafka
public class FlightKafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public FlightKafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    private Map<String, Object> baseConsumerProps() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, OrderReservationRequestedEvent> orderReservationRequestedConsumerFactory() {
        JsonDeserializer<OrderReservationRequestedEvent> valueDeserializer =
                new JsonDeserializer<>(OrderReservationRequestedEvent.class, false);
        valueDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(baseConsumerProps(),
                new StringDeserializer(), valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderReservationRequestedEvent>
    orderReservationRequestedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderReservationRequestedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderReservationRequestedConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderCancelledEvent> orderCancelledConsumerFactory() {
        JsonDeserializer<OrderCancelledEvent> valueDeserializer =
                new JsonDeserializer<>(OrderCancelledEvent.class, false);
        valueDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(baseConsumerProps(),
                new StringDeserializer(), valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent>
    orderCancelledKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderCancelledConsumerFactory());
        return factory;
    }
}

