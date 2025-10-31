# ----- Stage 1: Build -----
# Sử dụng base image chứa JDK (Java Development Kit) để build
FROM eclipse-temurin:17-jdk-alpine as builder

# Đặt thư mục làm việc
WORKDIR /app

# Copy các file build của Gradle (wrapper, build scripts)
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src ./src

# Cấp quyền thực thi cho gradlew
RUN chmod +x ./gradlew

# Build ứng dụng (sử dụng bootJar của Spring Boot và bỏ qua tests)
# --no-daemon để không chạy Gradle daemon trong container
RUN ./gradlew bootJar --no-daemon -x test

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