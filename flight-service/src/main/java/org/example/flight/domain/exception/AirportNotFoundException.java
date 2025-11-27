package org.example.flight.domain.exception;

/**
 * Domain Exception - Airport not found
 */
public class AirportNotFoundException extends RuntimeException {
    
    public AirportNotFoundException(String message) {
        super(message);
    }
    
    public AirportNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
