package org.example.flight.domain.exception;

/**
 * Domain Exception - Pure domain layer, no framework dependencies
 */
public class AircraftNotFoundException extends RuntimeException {
    
    public AircraftNotFoundException(String message) {
        super(message);
    }
    
    public AircraftNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
