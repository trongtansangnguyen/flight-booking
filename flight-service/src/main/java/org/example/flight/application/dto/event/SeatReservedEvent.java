package org.example.flight.application.dto.event;

import java.util.UUID;

public class SeatReservedEvent {
    private UUID bookingId;
    private UUID flightId;
    private int seatCount;

    public SeatReservedEvent() {
    }

    public SeatReservedEvent(UUID bookingId, UUID flightId, int seatCount) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.seatCount = seatCount;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public UUID getFlightId() {
        return flightId;
    }

    public void setFlightId(UUID flightId) {
        this.flightId = flightId;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }
}

