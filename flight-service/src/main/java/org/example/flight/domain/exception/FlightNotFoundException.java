package org.example.flight.domain.exception;

/**
 * Exception khi không tìm thấy Flight
 */
public class FlightNotFoundException extends RuntimeException {
    
    public FlightNotFoundException(String message) {
        super(message);
    }
    
    public FlightNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

