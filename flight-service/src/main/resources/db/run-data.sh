#!/bin/bash

# Script để chạy data.sql vào PostgreSQL

echo "🚀 Đang chạy script data.sql..."

# Kiểm tra container có đang chạy không
if ! docker ps | grep -q postgres-flight; then
    echo "❌ Container postgres-flight chưa chạy. Đang khởi động..."
    docker-compose up -d postgres-flight
    sleep 5
fi

# Đợi PostgreSQL sẵn sàng
echo "⏳ Đang đợi PostgreSQL sẵn sàng..."
until docker exec postgres-flight pg_isready -U postgres > /dev/null 2>&1; do
    echo "   Đang đợi..."
    sleep 2
done

# Chạy script
echo "📝 Đang insert dữ liệu..."
docker exec -i postgres-flight psql -U postgres -d flightdb < "$(dirname "$0")/data.sql"

if [ $? -eq 0 ]; then
    echo "✅ Đã insert dữ liệu thành công!"
    echo ""
    echo "📊 Kiểm tra số lượng dữ liệu:"
    docker exec -it postgres-flight psql -U postgres -d flightdb -c "SELECT 'airports' as table_name, COUNT(*) as count FROM airports UNION ALL SELECT 'aircrafts', COUNT(*) FROM aircrafts UNION ALL SELECT 'flights', COUNT(*) FROM flights;"
else
    echo "❌ Có lỗi xảy ra khi chạy script!"
    exit 1
fi

