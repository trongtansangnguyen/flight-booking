package org.example.flight.application.dto.event;

import java.util.UUID;

public class SeatReservationFailedEvent {
    private UUID bookingId;
    private UUID flightId;
    private int seatCount;
    private String reason;

    public SeatReservationFailedEvent() {
    }

    public SeatReservationFailedEvent(UUID bookingId, UUID flightId, int seatCount, String reason) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.seatCount = seatCount;
        this.reason = reason;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

