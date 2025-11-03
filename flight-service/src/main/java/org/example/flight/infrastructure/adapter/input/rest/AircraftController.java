package org.example.flight.infrastructure.adapter.input.rest;

import java.util.List;
import java.util.UUID;

import org.example.flight.application.dto.AircraftRequest;
import org.example.flight.application.dto.AircraftResponse;
import org.example.flight.application.port.input.AircraftUseCase;
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

/**
 * REST Controller - Infrastructure Layer (Input Adapter)
 * Phụ thuộc vào: Application Layer (Use Case interface)
 * Không phụ thuộc vào Domain trực tiếp
 * 
 * DEPENDENCY RULE: Infrastructure phụ thuộc Application
 */
@RestController
@RequestMapping("/api/aircrafts")
public class AircraftController {
    
    private final AircraftUseCase aircraftUseCase;
    
    public AircraftController(AircraftUseCase aircraftUseCase) {
        this.aircraftUseCase = aircraftUseCase;
    }
    
    @PostMapping
    public ResponseEntity<AircraftResponse> createAircraft(@RequestBody AircraftRequest request) {
        AircraftResponse response = aircraftUseCase.createAircraft(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AircraftResponse> updateAircraft(
            @PathVariable UUID id,
            @RequestBody AircraftRequest request) {
        AircraftResponse response = aircraftUseCase.updateAircraft(id, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AircraftResponse> getAircraftById(@PathVariable UUID id) {
        AircraftResponse response = aircraftUseCase.getAircraftById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<AircraftResponse>> getAllAircrafts() {
        List<AircraftResponse> responses = aircraftUseCase.getAllAircrafts();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/model/{model}")
    public ResponseEntity<List<AircraftResponse>> getAircraftsByModel(@PathVariable String model) {
        List<AircraftResponse> responses = aircraftUseCase.getAircraftsByModel(model);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/min-capacity/{minCapacity}")
    public ResponseEntity<List<AircraftResponse>> getAircraftsByMinCapacity(@PathVariable Integer minCapacity) {
        List<AircraftResponse> responses = aircraftUseCase.getAircraftsByMinCapacity(minCapacity);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<AircraftResponse>> searchAircrafts(@RequestParam String q) {
        List<AircraftResponse> responses = aircraftUseCase.searchAircrafts(q);
        return ResponseEntity.ok(responses);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable UUID id) {
        aircraftUseCase.deleteAircraft(id);
        return ResponseEntity.noContent().build();
    }
}
