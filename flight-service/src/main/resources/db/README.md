# Database Scripts

## Cách chạy script data.sql

### Cách 1: Chạy thủ công bằng psql (Khuyên dùng)

1. **Khởi động PostgreSQL container:**
```bash
docker-compose up -d postgres-flight
```

2. **Chạy script data.sql:**
```bash
# Lấy thông tin database từ .env file hoặc docker-compose.yml
docker exec -i postgres-flight psql -U postgres -d flightdb < flight-service/src/main/resources/db/data.sql
```

Hoặc nếu có file .env với POSTGRES_USER và POSTGRES_PASSWORD:
```bash
# Thay ${POSTGRES_USER} và ${POSTGRES_PASSWORD} bằng giá trị thực tế
docker exec -i postgres-flight psql -U ${POSTGRES_USER} -d flightdb < flight-service/src/main/resources/db/data.sql
```

### Cách 2: Chạy trực tiếp trong container

```bash
# Vào container
docker exec -it postgres-flight psql -U postgres -d flightdb

# Sau đó chạy script
\i /docker-entrypoint-initdb.d/data.sql
```

Hoặc copy file vào container và chạy:
```bash
docker cp flight-service/src/main/resources/db/data.sql postgres-flight:/tmp/data.sql
docker exec -it postgres-flight psql -U postgres -d flightdb -f /tmp/data.sql
```

### Cách 3: Thêm vào init.sql (Tự động chạy khi tạo DB mới)

Nếu muốn tự động chạy khi tạo database mới, thêm nội dung của `data.sql` vào cuối file `init.sql`.

**Lưu ý:** Cách này chỉ chạy khi database được tạo mới (lần đầu). Nếu database đã tồn tại, cần xóa volume và tạo lại:
```bash
docker-compose down -v
docker-compose up -d postgres-flight
```

### Kiểm tra dữ liệu đã insert

```bash
docker exec -it postgres-flight psql -U postgres -d flightdb -c "SELECT COUNT(*) FROM airports;"
docker exec -it postgres-flight psql -U postgres -d flightdb -c "SELECT COUNT(*) FROM aircrafts;"
docker exec -it postgres-flight psql -U postgres -d flightdb -c "SELECT COUNT(*) FROM flights;"
```

Mỗi table nên có 100 dòng.

