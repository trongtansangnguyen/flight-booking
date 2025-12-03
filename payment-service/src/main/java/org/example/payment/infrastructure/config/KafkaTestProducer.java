package org.example.payment.infrastructure.config;

import org.example.payment.presentation.messaging.dto.OrderCreatedEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Configuration
@Profile("test-producer") // Chỉ chạy khi bật profile "test-producer"
public class KafkaTestProducer {

    @Bean
    public CommandLineRunner sendTestMessage(KafkaTemplate<String, Object> kafkaTemplate) {
        return args -> {
            // 1. Tạo DTO (đối tượng Java)
            var orderId = UUID.randomUUID();
            var customerId = UUID.fromString("a1b2c3d4-1111-2222-3333-abcdef123456");
            var event = new OrderCreatedEvent(
                    orderId,
                    customerId,
                    new BigDecimal("15.75")
            );

            log.info("--- [TEST PRODUCER] ---");
            log.info("Đang gửi OrderCreatedEvent với orderId: {}", orderId);

            // 2. Gửi đi
            // KafkaTemplate (với JsonSerializer) sẽ TỰ ĐỘNG
            // thêm header __TypeId__ cho bạn.
            kafkaTemplate.send("order.created", orderId.toString(), event);

            log.info("Đã gửi message test!");
            log.info("-------------------------");
        };
    }
}

