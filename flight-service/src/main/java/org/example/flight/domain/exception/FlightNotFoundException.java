package org.example.flight.domain.exception;

/**
 * Domain Exception - Flight not found
 */
public class FlightNotFoundException extends RuntimeException {

    public FlightNotFoundException(String message) {
        super(message);
    }

    public FlightNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
