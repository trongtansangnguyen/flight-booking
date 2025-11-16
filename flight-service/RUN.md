# Hướng dẫn chạy Flight Service

## Cách 1: Chạy bằng Docker Compose (Khuyên dùng) 🐳

### Bước 1: Tạo file `.env` (nếu chưa có)

Tạo file `.env` ở thư mục root của project với nội dung:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```

### Bước 2: Khởi động tất cả services

```bash
# Từ thư mục root của project
docker-compose up -d
```

Hoặc chỉ chạy flight-service và dependencies:

```bash
docker-compose up -d postgres-flight flight-service
```

### Bước 3: Kiểm tra service đã chạy

```bash
# Xem logs
docker-compose logs -f flight-service

# Hoặc kiểm tra container
docker ps | grep flight-service
```

### Bước 4: Test API

Service sẽ chạy tại: `http://localhost:8083`

```bash
# Test health check
curl http://localhost:8083/actuator/health

# Test API airports
curl http://localhost:8083/api/airports

# Test API aircrafts
curl http://localhost:8083/api/aircrafts

# Test API flights
curl http://localhost:8083/api/flights
```

### Dừng service

```bash
docker-compose down
```

Hoặc chỉ dừng flight-service:

```bash
docker-compose stop flight-service
```

---

## Cách 2: Chạy trực tiếp bằng Gradle (Development) 🔧

### Yêu cầu

- Java 17+
- PostgreSQL đang chạy (port 5435)
- Gradle (hoặc dùng Gradle wrapper)

### Bước 1: Cấu hình database

Cập nhật `src/main/resources/application.properties`:

```properties
spring.application.name=flight-service
server.port=8081

# Database
spring.datasource.url=jdbc:postgresql://localhost:5435/flightdb
spring.datasource.username=postgres
spring.datasource.password=postgres

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### Bước 2: Đảm bảo PostgreSQL đang chạy

```bash
# Khởi động PostgreSQL
docker-compose up -d postgres-flight

# Hoặc chạy script data
./src/main/resources/db/run-data.sh
```

### Bước 3: Chạy service

```bash
# Từ thư mục flight-service
./gradlew bootRun

# Hoặc nếu dùng Windows
gradlew.bat bootRun
```

### Bước 4: Test API

Service sẽ chạy tại: `http://localhost:8081`

```bash
curl http://localhost:8081/api/airports
```

---

## Cách 3: Build và chạy JAR file 📦

### Bước 1: Build JAR

```bash
./gradlew bootJar
```

File JAR sẽ được tạo tại: `build/libs/flight-0.0.1-SNAPSHOT.jar`

### Bước 2: Chạy JAR

```bash
java -jar build/libs/flight-0.0.1-SNAPSHOT.jar
```

---

## Troubleshooting 🔍

### Lỗi: Cannot connect to database

**Nguyên nhân:** PostgreSQL chưa chạy hoặc sai cấu hình

**Giải pháp:**
```bash
# Kiểm tra PostgreSQL đang chạy
docker ps | grep postgres-flight

# Xem logs
docker-compose logs postgres-flight

# Khởi động lại
docker-compose restart postgres-flight
```

### Lỗi: Port already in use

**Nguyên nhân:** Port 8083 (hoặc 8081) đã được sử dụng

**Giải pháp:**
- Thay đổi port trong `application.properties`: `server.port=8084`
- Hoặc dừng service đang dùng port đó

### Lỗi: Table not found

**Nguyên nhân:** Database chưa có tables

**Giải pháp:**
```bash
# Chạy script init.sql
docker exec -i postgres-flight psql -U postgres -d flightdb < src/main/resources/db/init.sql

# Chạy script data.sql
docker exec -i postgres-flight psql -U postgres -d flightdb < src/main/resources/db/data.sql
```

### Kiểm tra logs

```bash
# Docker Compose
docker-compose logs -f flight-service

# Docker
docker logs -f flight-service

# Gradle
# Logs sẽ hiển thị trong terminal khi chạy ./gradlew bootRun
```

---

## API Endpoints 📡

Sau khi service chạy, các endpoints có sẵn:

### Airports
- `GET /api/airports` - Lấy tất cả airports
- `GET /api/airports/{id}` - Lấy airport theo ID
- `GET /api/airports/code/{code}` - Lấy airport theo code
- `POST /api/airports` - Tạo airport mới
- `PUT /api/airports/{id}` - Cập nhật airport
- `DELETE /api/airports/{id}` - Xóa airport

### Aircrafts
- `GET /api/aircrafts` - Lấy tất cả aircrafts
- `GET /api/aircrafts/{id}` - Lấy aircraft theo ID
- `POST /api/aircrafts` - Tạo aircraft mới
- `PUT /api/aircrafts/{id}` - Cập nhật aircraft
- `DELETE /api/aircrafts/{id}` - Xóa aircraft

### Flights
- `GET /api/flights` - Lấy tất cả flights
- `GET /api/flights/{id}` - Lấy flight theo ID
- `GET /api/flights/number/{number}` - Lấy flight theo số hiệu
- `POST /api/flights` - Tạo flight mới
- `PUT /api/flights/{id}` - Cập nhật flight
- `PATCH /api/flights/{id}/status` - Cập nhật trạng thái
- `DELETE /api/flights/{id}` - Xóa flight

---

## Quick Start 🚀

```bash
# 1. Tạo file .env
echo "POSTGRES_USER=postgres" > .env
echo "POSTGRES_PASSWORD=postgres" >> .env

# 2. Khởi động services
docker-compose up -d

# 3. Chờ service khởi động (khoảng 30 giây)
sleep 30

# 4. Test API
curl http://localhost:8083/api/airports
```

