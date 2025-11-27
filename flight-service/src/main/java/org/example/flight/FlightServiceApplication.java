package org.example.flight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "org.example.flight.infrastructure.adapter.input.rest",
    "org.example.flight.infrastructure.adapter.output.persistence",
    "org.example.flight.infrastructure.config"
})
public class FlightServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightServiceApplication.class, args);
    }

}
