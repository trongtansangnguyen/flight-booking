package org.example.order.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.application.ports.input.CancelExpiredReservationsUseCase;
import org.example.order.application.ports.output.OrderEventPublisher;
import org.example.order.application.ports.output.OrderRepository;
import org.example.order.domain.entity.Order;
import org.example.order.domain.entity.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Use Case Implementation for cancelling expired reservations
 * Application Layer - Business Logic
 * 
 * Finds orders with PENDING_PAYMENT status that have expired
 * and cancels them, releasing seats back to flight service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CancelExpiredReservationsUseCaseImpl implements CancelExpiredReservationsUseCase {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    @Transactional
    public int cancelExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        
        // Find all orders with PENDING_PAYMENT status that have expired
        List<Order> expiredOrders = orderRepository.findByStatusAndReservationExpiresAtBefore(
                OrderStatus.PENDING_PAYMENT, 
                now
        );
        
        if (expiredOrders.isEmpty()) {
            return 0;
        }
        
        log.info("Found {} expired reservation(s) to cancel", expiredOrders.size());
        
        int cancelledCount = 0;
        for (Order order : expiredOrders) {
            try {
                // Mark order as cancelled with reason
                order.markAsCancelled("Reservation expired");
                Order cancelledOrder = orderRepository.save(order);
                
                // Publish order.cancelled event to trigger compensation in Flight Service
                orderEventPublisher.publishOrderCancelled(cancelledOrder);
                
                log.info("Cancelled expired reservation for order: {}, flightId: {}, quantity: {}",
                        cancelledOrder.getId(), 
                        cancelledOrder.getFlightId(), 
                        cancelledOrder.getQuantityOfTickets());
                
                cancelledCount++;
            } catch (Exception e) {
                log.error("Error cancelling expired reservation for order: {}. Error: {}",
                        order.getId(), e.getMessage(), e);
                // Continue with other orders even if one fails
            }
        }
        
        return cancelledCount;
    }
}

