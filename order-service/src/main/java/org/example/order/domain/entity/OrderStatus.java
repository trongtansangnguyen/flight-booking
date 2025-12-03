package org.example.order.domain.entity;

/**
 * Order Status Enum - Domain Layer
 * Represents all possible states of an order in the booking flow
 */
public enum OrderStatus {
    RESERVING,          // Đang giữ chỗ (đã gửi request reservation đến Flight Service)
    PENDING_PAYMENT,    // Đang chờ thanh toán (đã reserve seat thành công)
    CONFIRMED,          // Đã xác nhận (đã thanh toán thành công)
    CANCELLED,          // Đã hủy (thanh toán thất bại hoặc timeout)
    FAILED              // Thất bại (không có chỗ trống)
}

