package org.example.flight.infrastructure.adapter.input.rest;

import java.util.List;
import java.util.UUID;

import org.example.flight.application.dto.AirportRequest;
import org.example.flight.application.dto.AirportResponse;
import org.example.flight.application.port.input.AirportUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final AirportUseCase airportUseCase;

    public AirportController(AirportUseCase airportUseCase) {
        this.airportUseCase = airportUseCase;
    }

    @PostMapping
    public ResponseEntity<AirportResponse> createAirport(@RequestBody AirportRequest request) {
        AirportResponse response = airportUseCase.createAirport(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportResponse> updateAirport(@PathVariable UUID id, @RequestBody AirportRequest request) {
        AirportResponse response = airportUseCase.updateAirport(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportResponse> getAirportById(@PathVariable UUID id) {
        AirportResponse response = airportUseCase.getAirportById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<AirportResponse> getAirportByCode(@PathVariable String code) {
        AirportResponse response = airportUseCase.getAirportByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AirportResponse>> getAllAirports() {
        List<AirportResponse> responses = airportUseCase.getAllAirports();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AirportResponse>> searchAirports(@RequestParam String q) {
        List<AirportResponse> responses = airportUseCase.searchAirports(q);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable UUID id) {
        airportUseCase.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }
}
