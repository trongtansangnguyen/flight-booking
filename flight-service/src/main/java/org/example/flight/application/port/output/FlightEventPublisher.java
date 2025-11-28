package org.example.flight.application.port.output;

import org.example.flight.application.dto.event.SeatReservationFailedEvent;
import org.example.flight.application.dto.event.SeatReservedEvent;

public interface FlightEventPublisher {
    void publishSeatReserved(SeatReservedEvent event);
    void publishSeatReservationFailed(SeatReservationFailedEvent event);
}

