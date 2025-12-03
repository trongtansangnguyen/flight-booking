# 🔄 Saga Choreography Workflow - Order Service Implementation

## 📋 Tổng Quan

Order Service được implement theo **Clean Architecture** và tham gia vào **Saga Choreography Pattern** để xử lý distributed transaction cho flight booking system.

---

## 🔄 Flow Chi Tiết

### **Bước 1-3: User Request Booking**

```
HTTP POST /api/orders
{
  "customerId": "uuid",
  "flightId": "uuid",
  "quantityOfTickets": 2,
  "totalPrice": 500.00
}
```

**Flow:**
1. `OrderController.createOrder()` nhận HTTP request
2. `CreateOrderUseCaseImpl.createOrder()` xử lý:
   - Tạo Order entity với status `RESERVING`
   - Lưu vào database
   - Publish event `order.reservation.requested` đến Kafka

**Event Published:**
```json
{
  "orderId": "uuid",
  "flightId": "uuid",
  "quantityOfTickets": 2
}
```
**Topic**: `order.reservation.requested`

---

### **Bước 4-5: Flight Service Xử Lý Seat Reservation**

**Flight Service** listen event và:
- Check available seats
- Nếu có chỗ → giảm `available_seats`, publish `seat.reserved`
- Nếu hết chỗ → publish `seat.reservation.failed`

---

### **Bước 6: Order Service Xử Lý Kết Quả Seat Reservation**

**Kafka Listeners:**

#### **Nếu thành công:**
```
Event: seat.reserved
  → SeatReservationResultListener.handleSeatReserved()
    → ProcessSeatReservationResultUseCaseImpl.handleSeatReserved()
      → Order.markAsPendingPayment()
      → OrderRepository.save()
      → OrderEventPublisher.publishOrderCreated()
```

**Event Published:**
```json
{
  "orderId": "uuid",
  "totalPrice": 500.00
}
```
**Topic**: `order.created`

#### **Nếu thất bại:**
```
Event: seat.reservation.failed
  → SeatReservationResultListener.handleSeatReservationFailed()
    → ProcessSeatReservationResultUseCaseImpl.handleSeatReservationFailed()
      → Order.markAsFailed(reason)
      → OrderRepository.save()
      → ❌ Flow kết thúc (không publish event)
```

---

### **Bước 7-10: Payment Service Xử Lý Payment**

**Payment Service** listen `order.created` và:
- Tạo payment với status `PENDING`
- User thực hiện payment
- Payment gateway báo kết quả
- Publish `payment.successful` hoặc `payment.failed`

---

### **Bước 11: Order Service Xử Lý Kết Quả Payment**

**Kafka Listeners:**

#### **Nếu thành công:**
```
Event: payment.successful
  → PaymentResultListener.handlePaymentSuccessful()
    → ProcessPaymentResultUseCaseImpl.handlePaymentSuccessful()
      → Order.markAsConfirmed()
      → OrderRepository.save()
      → OrderEventPublisher.publishOrderConfirmed()
```

**Event Published:**
```json
{
  "orderId": "uuid",
  "customerId": "uuid",
  "flightId": "uuid",
  "quantityOfTickets": 2
}
```
**Topic**: `order.confirmed`

#### **Nếu thất bại:**
```
Event: payment.failed
  → PaymentResultListener.handlePaymentFailed()
    → ProcessPaymentResultUseCaseImpl.handlePaymentFailed()
      → Order.markAsCancelled()
      → OrderRepository.save()
      → OrderEventPublisher.publishOrderCancelled()
```

**Event Published:**
```json
{
  "orderId": "uuid",
  "flightId": "uuid",
  "quantityOfTickets": 2
}
```
**Topic**: `order.cancelled`

---

### **Bước 12: Flight Service Compensation Transaction**

**Flight Service** listen `order.cancelled` và:
- Thêm lại `available_seats` (compensating transaction)

---

## 📊 Kafka Topics

### **Topics Order Service Publishes:**
1. `order.reservation.requested` → Flight Service
2. `order.created` → Payment Service
3. `order.confirmed` → Notifications
4. `order.cancelled` → Flight Service (compensation)

### **Topics Order Service Listens:**
1. `seat.reserved` ← Flight Service
2. `seat.reservation.failed` ← Flight Service
3. `payment.successful` ← Payment Service
4. `payment.failed` ← Payment Service

---

## 🏗️ Clean Architecture Layers

### **Domain Layer** 💎
- `Order` entity với business logic
- `OrderStatus` enum
- Domain exceptions

### **Application Layer** ⚙️
- Use Cases:
  - `CreateOrderUseCase` - Tạo order mới
  - `ProcessSeatReservationResultUseCase` - Xử lý kết quả seat reservation
  - `ProcessPaymentResultUseCase` - Xử lý kết quả payment
- Ports (Interfaces)
- DTOs

### **Infrastructure Layer** 🏗️
- `OrderController` - REST API
- Kafka Listeners:
  - `SeatReservationResultListener`
  - `PaymentResultListener`
- Kafka Publisher:
  - `OrderKafkaPublisherAdapter`
- Persistence:
  - `OrderJpaEntity`
  - `OrderRepositoryAdapter`

---

## ✅ Features Implemented

- ✅ Create order with RESERVING status
- ✅ Listen seat reservation results
- ✅ Update order status based on events
- ✅ Publish order events for other services
- ✅ Handle compensation flow
- ✅ Clean Architecture structure
- ✅ Domain-driven design
- ✅ Port & Adapter pattern

---

**Order Service sẵn sàng để tích hợp với Flight Service và Payment Service!** 🚀

