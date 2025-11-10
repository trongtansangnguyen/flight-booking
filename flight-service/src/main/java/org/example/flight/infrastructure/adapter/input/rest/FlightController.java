package org.example.flight.infrastructure.adapter.input.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.example.flight.application.dto.FlightRequest;
import org.example.flight.application.dto.FlightResponse;
import org.example.flight.application.port.input.FlightUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Flight operations
 */
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightUseCase flightUseCase;

    public FlightController(FlightUseCase flightUseCase) {
        this.flightUseCase = flightUseCase;
    }

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@RequestBody FlightRequest request) {
        FlightResponse response = flightUseCase.createFlight(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResponse> updateFlight(@PathVariable UUID id, @RequestBody FlightRequest request) {
        FlightResponse response = flightUseCase.updateFlight(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable UUID id) {
        FlightResponse response = flightUseCase.getFlightById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<FlightResponse> getFlightByNumber(@PathVariable String number) {
        FlightResponse response = flightUseCase.getFlightByNumber(number);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FlightResponse>> getAllFlights() {
        List<FlightResponse> responses = flightUseCase.getAllFlights();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponse>> searchFlights(
            @RequestParam(required = false) String originCode,
            @RequestParam(required = false) String destCode,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String status) {

        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;

        List<FlightResponse> responses = flightUseCase.searchFlights(originCode, destCode, start, end, status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<FlightResponse>> getUpcomingFlights() {
        List<FlightResponse> responses = flightUseCase.getUpcomingFlights();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FlightResponse> updateFlightStatus(@PathVariable UUID id, @RequestParam String status) {
        FlightResponse response = flightUseCase.updateFlightStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable UUID id) {
        flightUseCase.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}
