# Flight Service - Microservice

Microservice quản lý chuyến bay và duyệt vé bay trong hệ thống đặt vé máy bay.

## Chức năng chính

### 1. Quản lý chuyến bay (Flight Management)
- ✅ Tạo chuyến bay mới
- ✅ Cập nhật thông tin chuyến bay
- ✅ Xem chi tiết chuyến bay
- ✅ Tìm kiếm chuyến bay (theo điểm đi, điểm đến, thời gian)
- ✅ Xem danh sách chuyến bay sắp tới
- ✅ Cập nhật trạng thái chuyến bay (SCHEDULED, DELAYED, CANCELLED, COMPLETED)
- ✅ Xóa chuyến bay
- ✅ Kiểm tra số ghế trống

### 2. Quản lý vé bay (Ticket Management)
- ✅ Đặt vé (tạo ticket mới - status: PENDING)
- ✅ Duyệt vé (APPROVED) - tự động trừ số ghế trống
- ✅ Từ chối vé (REJECTED) với lý do
- ✅ Hủy vé (CANCELLED) - tự động hoàn lại số ghế
- ✅ Xem danh sách vé theo chuyến bay
- ✅ Xem danh sách vé theo trạng thái
- ✅ Xem vé theo email hành khách
- ✅ Tìm vé theo mã vé

## Kiến trúc (DI Pattern)

```
├── model/                    # Entity classes
│   ├── Flight.java          # Thông tin chuyến bay
│   ├── Ticket.java          # Thông tin vé
│   └── TicketStatus.java    # Enum: PENDING, APPROVED, REJECTED, CANCELLED
│
├── repository/              # Data Access Layer
│   ├── FlightRepository.java
│   └── TicketRepository.java
│
├── service/                 # Business Logic Layer
│   ├── FlightService.java          # Interface
│   ├── FlightServiceImpl.java      # Implementation với @RequiredArgsConstructor
│   ├── TicketService.java          # Interface
│   └── TicketServiceImpl.java      # Implementation với @RequiredArgsConstructor
│
├── controller/              # REST API Layer
│   ├── FlightController.java       # REST endpoints cho Flight
│   └── TicketController.java       # REST endpoints cho Ticket
│
├── dto/                     # Data Transfer Objects
│   ├── FlightRequest.java
│   ├── FlightResponse.java
│   ├── TicketRequest.java
│   ├── TicketResponse.java
│   └── TicketApprovalRequest.java
│
└── exception/              # Exception Handling
    ├── FlightNotFoundException.java
    ├── TicketNotFoundException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java
```

## Dependency Injection

Service được inject vào Controller bằng `@RequiredArgsConstructor` (Lombok):

```java
@RestController
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;  // Auto-injected
}

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;  // Auto-injected
}
```

## API Endpoints

### Flight APIs

```
POST   /api/flights                    # Tạo chuyến bay mới
PUT    /api/flights/{id}               # Cập nhật chuyến bay
GET    /api/flights/{id}               # Xem chi tiết chuyến bay
GET    /api/flights/number/{number}    # Tìm chuyến bay theo số hiệu
GET    /api/flights                    # Xem tất cả chuyến bay
GET    /api/flights/search             # Tìm kiếm (origin, destination, startDate, endDate)
GET    /api/flights/upcoming           # Xem chuyến bay sắp tới
DELETE /api/flights/{id}               # Xóa chuyến bay
PATCH  /api/flights/{id}/status        # Cập nhật trạng thái
GET    /api/flights/{id}/available-seats  # Kiểm tra ghế trống
```

### Ticket APIs

```
POST   /api/tickets                    # Đặt vé mới
GET    /api/tickets/{id}               # Xem chi tiết vé
GET    /api/tickets/number/{number}    # Tìm vé theo mã
GET    /api/tickets                    # Xem tất cả vé
GET    /api/tickets/flight/{flightId}  # Xem vé theo chuyến bay
GET    /api/tickets/status/{status}    # Xem vé theo trạng thái
GET    /api/tickets/email/{email}      # Xem vé theo email
POST   /api/tickets/{id}/approve       # Duyệt vé
POST   /api/tickets/{id}/reject        # Từ chối vé
DELETE /api/tickets/{id}               # Hủy vé
```

## Cài đặt và chạy

### Yêu cầu
- Java 17+
- PostgreSQL
- Kafka (optional)

### 1. Tạo database

```sql
CREATE DATABASE flight_db;
```

### 2. Cấu hình

Chỉnh sửa `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/flight_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Chạy service

```bash
./gradlew bootRun
```

Service sẽ chạy tại: `http://localhost:8081`

## Ví dụ sử dụng

### Tạo chuyến bay mới

```bash
curl -X POST http://localhost:8081/api/flights \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "VN123",
    "origin": "HAN",
    "destination": "SGN",
    "departureTime": "2024-12-01T10:00:00",
    "arrivalTime": "2024-12-01T12:00:00",
    "totalSeats": 180,
    "price": 1500000
  }'
```

### Đặt vé

```bash
curl -X POST http://localhost:8081/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "flightId": 1,
    "passengerName": "Nguyen Van A",
    "passengerEmail": "nguyenvana@email.com",
    "passengerPhone": "0901234567",
    "seatNumber": "12A"
  }'
```

### Duyệt vé

```bash
curl -X POST http://localhost:8081/api/tickets/1/approve \
  -H "Content-Type: application/json" \
  -d '{
    "approved": true,
    "approvedBy": "admin@airline.com"
  }'
```

### Từ chối vé

```bash
curl -X POST http://localhost:8081/api/tickets/1/reject \
  -H "Content-Type: application/json" \
  -d '{
    "approved": false,
    "approvedBy": "admin@airline.com",
    "rejectionReason": "Thông tin hành khách không hợp lệ"
  }'
```

### Tìm kiếm chuyến bay

```bash
curl "http://localhost:8081/api/flights/search?origin=HAN&destination=SGN&startDate=2024-12-01T00:00:00&endDate=2024-12-31T23:59:59"
```

## Luồng xử lý chính

### Quy trình đặt và duyệt vé:

1. **Khách hàng đặt vé** → POST /api/tickets
   - Tạo ticket với status = PENDING
   - Chưa trừ số ghế available

2. **Admin duyệt vé** → POST /api/tickets/{id}/approve
   - Cập nhật status = APPROVED
   - Tự động trừ availableSeats của Flight
   - Lưu thông tin người duyệt và thời gian

3. **Hoặc Admin từ chối** → POST /api/tickets/{id}/reject
   - Cập nhật status = REJECTED
   - Không ảnh hưởng số ghế
   - Lưu lý do từ chối

4. **Khách hủy vé** → DELETE /api/tickets/{id}
   - Cập nhật status = CANCELLED
   - Nếu vé đã APPROVED → hoàn lại số ghế

## Tính năng nổi bật

✅ **Dependency Injection**: Sử dụng @RequiredArgsConstructor để tự động inject dependencies  
✅ **Transaction Management**: @Transactional để đảm bảo tính toàn vẹn dữ liệu  
✅ **Exception Handling**: GlobalExceptionHandler xử lý tập trung  
✅ **Automatic Seat Management**: Tự động quản lý số ghế khi duyệt/hủy vé  
✅ **Audit Trail**: Lưu người duyệt, thời gian duyệt, lý do từ chối  
✅ **Validation**: Kiểm tra điều kiện trước khi thực hiện action  

## Technologies

- **Spring Boot 3.5.7**
- **Spring Data JPA** - ORM
- **PostgreSQL** - Database
- **Lombok** - Boilerplate code reduction
- **Spring Kafka** - Message broker (optional)
- **Hibernate** - JPA implementation

## Contact

Developed with ❤️ for Flight Booking System
