package org.example.order.infrastructure.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.ports.input.CancelExpiredReservationsUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled Job to cancel expired seat reservations
 * Infrastructure Layer - Scheduled Task
 * 
 * Runs every minute to check for orders with PENDING_PAYMENT status
 * that have passed their reservation expiry time
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredReservationCleanupJob {

    private final CancelExpiredReservationsUseCase cancelExpiredReservationsUseCase;

    /**
     * Run every minute to check and cancel expired reservations
     * Fixed rate: 60000ms = 1 minute
     */
    @Scheduled(fixedRate = 60000)
    public void cancelExpiredReservations() {
        log.debug("Running expired reservation cleanup job");
        try {
            int cancelledCount = cancelExpiredReservationsUseCase.cancelExpiredReservations();
            if (cancelledCount > 0) {
                log.info("Cancelled {} expired reservation(s)", cancelledCount);
            }
        } catch (Exception e) {
            log.error("Error cancelling expired reservations", e);
        }
    }
}

