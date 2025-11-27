# Flight Service - Microservice

Microservice quản lý chuyến bay và duyệt vé bay trong hệ thống đặt vé máy bay.

## Database Schema

### 1. Table `airports`
| Column | Type | Description |
|--------|------|-------------|
| airport_id | UUID | PK |
| code | VARCHAR(10) | Mã IATA (SGN, HAN) |
| name | VARCHAR(255) | Tên sân bay (Tân Sơn Nhất) |
| city | VARCHAR(100) | Thành phố |
| country | VARCHAR(100) | Quốc gia |

### 2. Table `aircrafts`
| Column | Type | Description |
|--------|------|-------------|
| aircraft_id | UUID | PK |
| model | VARCHAR(100) | Kiểu máy bay (Airbus A320) |
| name | VARCHAR(255) | Tên máy bay |
| capacity | INT | Sức chứa |

### 3. Table `flights`
| Column | Type | Description |
|--------|------|-------------|
| flight_id | UUID | PK |
| flight_number | VARCHAR(20) | Mã chuyến bay (VN123) |
| origin_airport_id | UUID | (FK) ID sân bay cất cánh |
| dest_airport_id | UUID | (FK) ID sân bay hạ cánh |
| departure_time | TIMESTAMP | Thời gian cất cánh (UTC) |
| arrival_time | TIMESTAMP | Thời gian hạ cánh (UTC) |
| aircraft_id | UUID | (FK) ID máy bay |
| base_price | DECIMAL(10,2) | Giá vé cơ bản |
| available_seats | INT | Số ghế còn trống |
| status | VARCHAR(20) | SCHEDULED, DELAYED, CANCELLED |

### 4. Table `tickets`
| Column | Type | Description |
|--------|------|-------------|
| ticket_id | UUID | PK |
| ticket_number | VARCHAR(50) | Mã vé (TK-XXXX) |
| flight_id | UUID | (FK) ID chuyến bay |
| passenger_name | VARCHAR(255) | Tên hành khách |
| passenger_email | VARCHAR(255) | Email hành khách |
| passenger_phone | VARCHAR(20) | SĐT hành khách |
| seat_number | VARCHAR(10) | Số ghế (12A) |
| status | VARCHAR(20) | PENDING, APPROVED, REJECTED, CANCELLED |
| price | DECIMAL(10,2) | Giá vé |
| approved_by | VARCHAR(255) | Người duyệt |
| approved_at | TIMESTAMP | Thời gian duyệt |
| rejection_reason | TEXT | Lý do từ chối |

## Entity Relationships

```
Airport (1) ----< (*) Flight [origin_airport_id]
Airport (1) ----< (*) Flight [dest_airport_id]
Aircraft (1) ----< (*) Flight
Flight (1) ----< (*) Ticket
```

## Kiến trúc (DI Pattern)

```
├── model/                         # Entity classes
│   ├── Airport.java               # Sân bay
│   ├── Aircraft.java              # Máy bay
│   ├── Flight.java                # Chuyến bay
│   ├── FlightStatus.java          # Enum: SCHEDULED, DELAYED, CANCELLED
│   ├── Ticket.java                # Vé
│   └── TicketStatus.java          # Enum: PENDING, APPROVED, REJECTED, CANCELLED
│
├── repository/                    # Data Access Layer
│   ├── AirportRepository.java
│   ├── AircraftRepository.java
│   ├── FlightRepository.java
│   └── TicketRepository.java
│
├── service/                       # Business Logic Layer
│   ├── AirportService.java + AirportServiceImpl.java
│   ├── AircraftService.java + AircraftServiceImpl.java
│   ├── FlightService.java + FlightServiceImpl.java
│   └── TicketService.java + TicketServiceImpl.java
│
├── controller/                    # REST API Layer
│   ├── AirportController.java
│   ├── AircraftController.java
│   ├── FlightController.java
│   └── TicketController.java
│
├── dto/                           # Data Transfer Objects
│   ├── AirportRequest/Response
│   ├── AircraftRequest/Response
│   ├── FlightRequest/Response
│   ├── TicketRequest/Response
│   └── TicketApprovalRequest
│
└── exception/                     # Exception Handling
    ├── FlightNotFoundException.java
    ├── TicketNotFoundException.java
    ├── AirportNotFoundException.java
    ├── AircraftNotFoundException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java
```

## Chức năng chính

### 1. Quản lý Sân bay (Airport Management)
- ✅ CRUD sân bay
- ✅ Tìm kiếm sân bay theo code (IATA), tên, thành phố, quốc gia
- ✅ Xem danh sách chuyến bay từ/đến sân bay

### 2. Quản lý Máy bay (Aircraft Management)
- ✅ CRUD máy bay
- ✅ Tìm kiếm máy bay theo model, capacity
- ✅ Xem danh sách chuyến bay sử dụng máy bay

### 3. Quản lý Chuyến bay (Flight Management)
- ✅ Tạo chuyến bay với airport và aircraft references
- ✅ Cập nhật thông tin chuyến bay
- ✅ Xem chi tiết chuyến bay (bao gồm thông tin airport và aircraft)
- ✅ Tìm kiếm chuyến bay theo:
  - Điểm đi (origin airport code)
  - Điểm đến (destination airport code)
  - Thời gian bay
  - Trạng thái
- ✅ Xem chuyến bay sắp tới còn ghế trống
- ✅ Cập nhật trạng thái: SCHEDULED, DELAYED, CANCELLED
- ✅ Tự động set available_seats = aircraft.capacity khi tạo

### 4. Quản lý Vé (Ticket Management)
- ✅ Đặt vé (status: PENDING)
- ✅ Duyệt vé (APPROVED) - trừ available_seats
- ✅ Từ chối vé (REJECTED) với lý do
- ✅ Hủy vé (CANCELLED) - hoàn lại ghế
- ✅ Xem vé theo chuyến bay, trạng thái, email

## API Endpoints

### Airport APIs
```
POST   /api/airports              # Tạo sân bay
GET    /api/airports/{id}         # Xem chi tiết
GET    /api/airports/code/{code}  # Tìm theo IATA code
GET    /api/airports              # Xem tất cả
GET    /api/airports/search?q=    # Tìm kiếm
PUT    /api/airports/{id}         # Cập nhật
DELETE /api/airports/{id}         # Xóa
```

### Aircraft APIs
```
POST   /api/aircrafts             # Tạo máy bay
GET    /api/aircrafts/{id}        # Xem chi tiết
GET    /api/aircrafts             # Xem tất cả
GET    /api/aircrafts/search?q=   # Tìm kiếm
PUT    /api/aircrafts/{id}        # Cập nhật
DELETE /api/aircrafts/{id}        # Xóa
```

### Flight APIs
```
POST   /api/flights                           # Tạo chuyến bay
PUT    /api/flights/{id}                      # Cập nhật
GET    /api/flights/{id}                      # Chi tiết (với airport & aircraft info)
GET    /api/flights/number/{number}           # Tìm theo số hiệu
GET    /api/flights                           # Tất cả
GET    /api/flights/search                    # Tìm kiếm
     ?originCode=SGN
     &destCode=HAN
     &startDate=2024-12-01T00:00:00
     &endDate=2024-12-31T23:59:59
GET    /api/flights/upcoming                  # Chuyến bay sắp tới
PATCH  /api/flights/{id}/status?status=       # Cập nhật trạng thái
DELETE /api/flights/{id}                      # Xóa
```

### Ticket APIs
```
POST   /api/tickets                    # Đặt vé
GET    /api/tickets/{id}               # Chi tiết
GET    /api/tickets/number/{number}    # Tìm theo mã vé
GET    /api/tickets                    # Tất cả
GET    /api/tickets/flight/{flightId}  # Theo chuyến bay
GET    /api/tickets/status/{status}    # Theo trạng thái
GET    /api/tickets/email/{email}      # Theo email
POST   /api/tickets/{id}/approve       # Duyệt vé
POST   /api/tickets/{id}/reject        # Từ chối
DELETE /api/tickets/{id}               # Hủy vé
```

## Ví dụ sử dụng

### 1. Tạo sân bay
```bash
curl -X POST http://localhost:8081/api/airports \
  -H "Content-Type: application/json" \
  -d '{
    "code": "SGN",
    "name": "Sân bay Quốc tế Tân Sơn Nhất",
    "city": "Hồ Chí Minh",
    "country": "Việt Nam"
  }'
```

### 2. Tạo máy bay
```bash
curl -X POST http://localhost:8081/api/aircrafts \
  -H "Content-Type: application/json" \
  -d '{
    "model": "Airbus A320",
    "name": "VN-A321",
    "capacity": 180
  }'
```

### 3. Tạo chuyến bay
```bash
curl -X POST http://localhost:8081/api/flights \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "VN123",
    "originAirportId": "uuid-airport-sgn",
    "destinationAirportId": "uuid-airport-han",
    "departureTime": "2024-12-01T10:00:00",
    "arrivalTime": "2024-12-01T12:00:00",
    "aircraftId": "uuid-aircraft-a320",
    "basePrice": 1500000.00
  }'
```

Response sẽ bao gồm đầy đủ thông tin airport và aircraft:
```json
{
  "id": "uuid",
  "flightNumber": "VN123",
  "originAirportId": "uuid",
  "originCode": "SGN",
  "originName": "Sân bay Quốc tế Tân Sơn Nhất",
  "originCity": "Hồ Chí Minh",
  "destinationAirportId": "uuid",
  "destinationCode": "HAN",
  "destinationName": "Sân bay Quốc tế Nội Bài",
  "destinationCity": "Hà Nội",
  "departureTime": "2024-12-01T10:00:00",
  "arrivalTime": "2024-12-01T12:00:00",
  "aircraftId": "uuid",
  "aircraftModel": "Airbus A320",
  "aircraftName": "VN-A321",
  "aircraftCapacity": 180,
  "basePrice": 1500000.00,
  "availableSeats": 180,
  "status": "SCHEDULED"
}
```

### 4. Đặt vé
```bash
curl -X POST http://localhost:8081/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "flightId": "uuid-flight",
    "passengerName": "Nguyen Van A",
    "passengerEmail": "nguyenvana@email.com",
    "passengerPhone": "0901234567",
    "seatNumber": "12A"
  }'
```

### 5. Tìm kiếm chuyến bay
```bash
curl "http://localhost:8081/api/flights/search?originCode=SGN&destCode=HAN&startDate=2024-12-01T00:00:00&endDate=2024-12-31T23:59:59"
```

## Cấu hình Database

Cập nhật `application.properties`:

```properties
spring.application.name=flight-service
server.port=8081

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/flight_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Đặc điểm thiết kế

✅ **UUID Primary Keys**: Sử dụng UUID thay vì Long để phân tán tốt hơn  
✅ **BigDecimal for Money**: Sử dụng BigDecimal cho giá tiền (chính xác)  
✅ **Foreign Key Relationships**: ManyToOne relationships với Airport và Aircraft  
✅ **Enum Types**: FlightStatus và TicketStatus cho type-safe  
✅ **Rich Response DTOs**: FlightResponse bao gồm đầy đủ thông tin nested  
✅ **Dependency Injection**: @RequiredArgsConstructor cho clean code  
✅ **Transaction Management**: @Transactional cho data consistency  

## Technologies

- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **UUID** for primary keys
- **BigDecimal** for monetary values
