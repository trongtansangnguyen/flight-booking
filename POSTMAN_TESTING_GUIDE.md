# 🧪 Hướng Dẫn Test Saga Choreography Workflow bằng Postman

## 📋 Mục Lục
1. [Chuẩn Bị](#chuẩn-bị)
2. [Setup Dữ Liệu Test](#setup-dữ-liệu-test)
3. [Test Flow Happy Path](#test-flow-happy-path)
4. [Test Flow Failure Cases](#test-flow-failure-cases)
5. [Verify Kafka Events](#verify-kafka-events)

---

## 🚀 Chuẩn Bị

### 1. Khởi động Services

```bash
# Khởi động tất cả services bằng Docker Compose
docker-compose up -d

# Kiểm tra services đang chạy
docker ps
```

**Services và Ports:**
- **Flight Service**: `http://localhost:8081`
- **Order Service**: `http://localhost:8082`
- **Payment Service**: `http://localhost:8084`
- **Kafka UI**: `http://localhost:8080`

### 2. Import Postman Collection

Tạo các requests sau trong Postman:

---

## 📦 Setup Dữ Liệu Test

### **Step 1: Tạo Airport (Origin)**

**Request:**
```
POST http://localhost:8081/api/airports
Content-Type: application/json
```

**Body:**
```json
{
  "code": "SGN",
  "name": "Tan Son Nhat International Airport",
  "city": "Ho Chi Minh City",
  "country": "Vietnam"
}
```

**Response:** Lưu `id` của airport (ví dụ: `originAirportId`)

---

### **Step 2: Tạo Airport (Destination)**

**Request:**
```
POST http://localhost:8081/api/airports
Content-Type: application/json
```

**Body:**
```json
{
  "code": "HAN",
  "name": "Noi Bai International Airport",
  "city": "Hanoi",
  "country": "Vietnam"
}
```

**Response:** Lưu `id` của airport (ví dụ: `destinationAirportId`)

---

### **Step 3: Tạo Aircraft**

**Request:**
```
POST http://localhost:8081/api/aircrafts
Content-Type: application/json
```

**Body:**
```json
{
  "model": "Boeing 737-800",
  "name": "VN-A123",
  "capacity": 180,
  "manufacturer": "Boeing"
}
```

**Response:** Lưu `id` của aircraft (ví dụ: `aircraftId`)

---

### **Step 4: Tạo Flight**

**Request:**
```
POST http://localhost:8081/api/flights
Content-Type: application/json
```

**Body:**
```json
{
  "flightNumber": "VN123",
  "originAirportId": "<originAirportId>",
  "destinationAirportId": "<destinationAirportId>",
  "departureTime": "2024-12-25T10:00:00",
  "arrivalTime": "2024-12-25T12:00:00",
  "aircraftId": "<aircraftId>",
  "basePrice": 5000000
}
```

**Response:** Lưu `id` của flight (ví dụ: `flightId`)

**Verify:**
```
GET http://localhost:8081/api/flights/<flightId>
```
Kiểm tra `availableSeats` = 180 (capacity của aircraft)

---

### **Step 5: Setup Customer Credit (Payment Service)**

**Lưu ý:** Payment Service không có REST API để tạo customer credit. Bạn cần tạo trực tiếp trong database:

**SQL Script:**
```sql
-- Kết nối đến paymentdb (port 5434)
INSERT INTO customer_credits (customer_id, credit_limit, current_balance)
VALUES 
  ('550e8400-e29b-41d4-a716-446655440000', 10000000, 10000000),
  ('550e8400-e29b-41d4-a716-446655440001', 5000000, 5000000);  -- Customer với credit thấp để test failure
```

**Hoặc sử dụng UUID generator:**
- Customer ID 1 (đủ credit): `550e8400-e29b-41d4-a716-446655440000`
- Customer ID 2 (thiếu credit): `550e8400-e29b-41d4-a716-446655440001`

---

## ✅ Test Flow Happy Path

### **Scenario: Đặt vé thành công → Thanh toán thành công**

---

### **Step 1: Tạo Order (Order Service)**

**Request:**
```
POST http://localhost:8082/api/orders
Content-Type: application/json
```

**Body:**
```json
{
  "customerId": "550e8400-e29b-41d4-a716-446655440000",
  "flightId": "<flightId>",
  "quantityOfTickets": 2,
  "totalPrice": 10000000
}
```

**Expected Response (201 Created):**
```json
{
  "id": "<orderId>",
  "customerId": "550e8400-e29b-41d4-a716-446655440000",
  "flightId": "<flightId>",
  "quantityOfTickets": 2,
  "totalPrice": 10000000,
  "status": "RESERVING",
  "failureReason": null,
  "createdAt": "2024-12-02T12:00:00",
  "updatedAt": "2024-12-02T12:00:00"
}
```

**✅ Verify:**
- Status = `RESERVING`
- Order được tạo thành công

**📡 Kafka Event:** `order.reservation.requested` được publish

---

### **Step 2: Verify Seat Reservation (Flight Service)**

**Wait 2-3 seconds** để Flight Service xử lý event, sau đó:

**Request:**
```
GET http://localhost:8081/api/flights/<flightId>
```

**Expected Response:**
```json
{
  "id": "<flightId>",
  "availableSeats": 178,  // Giảm từ 180 xuống 178 (đã reserve 2 seats)
  ...
}
```

**✅ Verify:**
- `availableSeats` giảm đúng số lượng tickets
- Flight Service đã xử lý `order.reservation.requested`

**📡 Kafka Event:** `seat.reserved` được publish

---

### **Step 3: Verify Order Status Updated (Order Service)**

**Request:**
```
GET http://localhost:8082/api/orders/<orderId>
```

**Expected Response:**
```json
{
  "id": "<orderId>",
  "status": "PENDING_PAYMENT",  // Đã update từ RESERVING
  ...
}
```

**✅ Verify:**
- Status = `PENDING_PAYMENT`
- Order Service đã nhận `seat.reserved` event

**📡 Kafka Event:** `order.created` được publish

---

### **Step 4: Verify Payment Processing (Payment Service)**

**Wait 2-3 seconds** để Payment Service xử lý `order.created` event.

**Kiểm tra logs của Payment Service:**
```bash
docker logs payment-service -f
```

**Expected Logs:**
```
Processing payment for order: <orderId>
Payment COMPLETED for order: <orderId>
Publishing PaymentSuccessful event for order: <orderId>
```

**📡 Kafka Event:** `payment.successful` được publish

---

### **Step 5: Verify Order Confirmed (Order Service)**

**Request:**
```
GET http://localhost:8082/api/orders/<orderId>
```

**Expected Response:**
```json
{
  "id": "<orderId>",
  "status": "CONFIRMED",  // Đã update từ PENDING_PAYMENT
  ...
}
```

**✅ Verify:**
- Status = `CONFIRMED`
- Order Service đã nhận `payment.successful` event

**📡 Kafka Event:** `order.confirmed` được publish (để gửi email/notification)

---

## ❌ Test Flow Failure Cases

### **Scenario 1: Hết vé (Seat Reservation Failed)**

---

### **Step 1: Tạo Flight với ít seats**

Tạo một flight mới hoặc update flight hiện tại để có ít seats:

**Request:**
```
PATCH http://localhost:8081/api/flights/<flightId>/status?status=SCHEDULED
```

**Hoặc tạo order với số lượng lớn hơn available seats**

---

### **Step 2: Tạo Order với số lượng vé vượt quá available seats**

**Request:**
```
POST http://localhost:8082/api/orders
Content-Type: application/json
```

**Body:**
```json
{
  "customerId": "550e8400-e29b-41d4-a716-446655440000",
  "flightId": "<flightId>",
  "quantityOfTickets": 200,  // Vượt quá available seats (180)
  "totalPrice": 1000000000
}
```

**Expected Response (201 Created):**
```json
{
  "status": "RESERVING",
  ...
}
```

---

### **Step 3: Verify Order Failed**

**Wait 2-3 seconds**, sau đó:

**Request:**
```
GET http://localhost:8082/api/orders/<orderId>
```

**Expected Response:**
```json
{
  "id": "<orderId>",
  "status": "FAILED",
  "failureReason": "sold_out",
  ...
}
```

**✅ Verify:**
- Status = `FAILED`
- `failureReason` = "sold_out"
- Flight Service đã publish `seat.reservation.failed`

**📡 Kafka Event:** `seat.reservation.failed` được publish

---

### **Scenario 2: Payment Failed (Insufficient Credit)**

---

### **Step 1: Tạo Order với Customer có credit thấp**

**Request:**
```
POST http://localhost:8082/api/orders
Content-Type: application/json
```

**Body:**
```json
{
  "customerId": "550e8400-e29b-41d4-a716-446655440001",  // Customer với credit thấp
  "flightId": "<flightId>",
  "quantityOfTickets": 2,
  "totalPrice": 10000000  // Vượt quá credit limit (5,000,000)
}
```

**Expected Response:**
```json
{
  "status": "RESERVING",
  ...
}
```

---

### **Step 2: Verify Seat Reserved**

**Request:**
```
GET http://localhost:8081/api/flights/<flightId>
```

**✅ Verify:** `availableSeats` đã giảm

---

### **Step 3: Verify Order Pending Payment**

**Request:**
```
GET http://localhost:8082/api/orders/<orderId>
```

**✅ Verify:** Status = `PENDING_PAYMENT`

---

### **Step 4: Verify Payment Failed**

**Wait 2-3 seconds**, kiểm tra logs:

```bash
docker logs payment-service -f
```

**Expected Logs:**
```
Payment FAILED for order: <orderId>. Reason: Insufficient credit balance...
Publishing PaymentFailed event for order: <orderId>
```

**📡 Kafka Event:** `payment.failed` được publish

---

### **Step 5: Verify Order Cancelled & Seats Released**

**Request:**
```
GET http://localhost:8082/api/orders/<orderId>
```

**Expected Response:**
```json
{
  "status": "CANCELLED",
  ...
}
```

**Request:**
```
GET http://localhost:8081/api/flights/<flightId>
```

**Expected Response:**
```json
{
  "availableSeats": 180,  // Đã được release lại (compensation)
  ...
}
```

**✅ Verify:**
- Order status = `CANCELLED`
- Flight `availableSeats` đã được tăng lại (compensation transaction)
- Flight Service đã nhận `order.cancelled` event

**📡 Kafka Event:** `order.cancelled` được publish

---

## 🔍 Verify Kafka Events

### **Cách 1: Sử dụng Kafka UI**

1. Mở browser: `http://localhost:8080`
2. Chọn cluster: `local`
3. Vào **Topics** → Chọn topic cần xem
4. Click **Messages** để xem events

**Topics cần kiểm tra:**
- `order.reservation.requested`
- `seat.reserved`
- `seat.reservation.failed`
- `order.created`
- `payment.successful`
- `payment.failed`
- `order.confirmed`
- `order.cancelled`

---

### **Cách 2: Sử dụng Kafka Console Consumer**

```bash
# Xem messages trong topic
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic order.reservation.requested \
  --from-beginning

# Xem messages trong topic seat.reserved
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic seat.reserved \
  --from-beginning
```

---

## 📊 Test Summary Checklist

### **Happy Path:**
- [ ] Order created với status `RESERVING`
- [ ] Flight `availableSeats` giảm
- [ ] Order status chuyển sang `PENDING_PAYMENT`
- [ ] Payment processed thành công
- [ ] Order status chuyển sang `CONFIRMED`
- [ ] Events được publish đúng thứ tự

### **Failure Case 1: Hết vé**
- [ ] Order created với status `RESERVING`
- [ ] Order status chuyển sang `FAILED` với reason "sold_out"
- [ ] `seat.reservation.failed` event được publish

### **Failure Case 2: Payment Failed**
- [ ] Order created và seat reserved
- [ ] Order status chuyển sang `PENDING_PAYMENT`
- [ ] Payment failed
- [ ] Order status chuyển sang `CANCELLED`
- [ ] Flight `availableSeats` được release lại (compensation)
- [ ] `order.cancelled` event được publish

---

## 🐛 Troubleshooting

### **Order không chuyển status:**
- Kiểm tra Kafka đang chạy: `docker ps | grep kafka`
- Kiểm tra logs của services: `docker logs <service-name> -f`
- Verify Kafka topics được tạo: `docker exec -it kafka kafka-topics --list --bootstrap-server localhost:9092`

### **Payment không được process:**
- Kiểm tra customer credit đã được tạo trong database
- Verify `order.created` event được publish
- Kiểm tra Payment Service logs

### **Seats không được release:**
- Kiểm tra `order.cancelled` event được publish
- Verify Flight Service listener đang chạy
- Kiểm tra Flight Service logs

---

## 📝 Notes

1. **Timing:** Các events được xử lý bất đồng bộ, cần đợi 2-3 giây giữa các bước
2. **UUIDs:** Sử dụng UUID thật từ responses, không hardcode
3. **Database:** Customer credit phải được setup trước khi test payment
4. **Kafka:** Đảm bảo Kafka đang chạy trước khi test

---

**Happy Testing! 🚀**

