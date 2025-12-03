# ----- Stage 1: Build -----
FROM eclipse-temurin:17-jdk-alpine as builder

WORKDIR /app

# Copy Gradle wrapper và build files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Cấp quyền thực thi cho gradlew
RUN chmod +x ./gradlew

# Download dependencies trước (để cache layer này)
RUN ./gradlew dependencies --no-daemon --info || true

# Copy source code
COPY src ./src

# Build ứng dụng với verbose output
RUN ./gradlew bootJar --no-daemon --info --stacktrace -x test

# ----- Stage 2: Runtime -----
# Sử dụng base image chỉ chứa JRE (Java Runtime Environment)
FROM eclipse-temurin:17-jre-alpine

# Đặt thư mục làm việc
WORKDIR /app

# Copy file .jar đã build từ stage 'builder'
# Gradle lưu file build trong build/libs/
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port 8080 (port mặc định của Spring Boot)
EXPOSE 8080

# Lệnh để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]