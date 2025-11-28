package org.example.flight.application.dto.event;

import java.util.UUID;

public class OrderCancelledEvent {
    private UUID flightId;
    private UUID bookingId;
    private int seatCount;

    public OrderCancelledEvent() {
    }

    public OrderCancelledEvent(UUID flightId, UUID bookingId, int seatCount) {
        this.flightId = flightId;
        this.bookingId = bookingId;
        this.seatCount = seatCount;
    }

    public UUID getFlightId() {
        return flightId;
    }

    public void setFlightId(UUID flightId) {
        this.flightId = flightId;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }
}

