package org.example.flight;

import org.example.flight.application.dto.event.SeatReservationFailedEvent;
import org.example.flight.application.dto.event.SeatReservedEvent;
import org.example.flight.application.port.output.FlightEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(FlightServiceApplicationTests.MessagingTestConfig.class)
class FlightServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @TestConfiguration
    @Profile("test")
    static class MessagingTestConfig {
        @Bean
        FlightEventPublisher flightEventPublisher() {
            return new FlightEventPublisher() {
                @Override
                public void publishSeatReserved(SeatReservedEvent event) {
                    // no-op for tests
                }

                @Override
                public void publishSeatReservationFailed(SeatReservationFailedEvent event) {
                    // no-op for tests
                }
            };
        }
    }
}
