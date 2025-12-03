# ----- Stage 1: Build (Tối ưu hóa) -----
FROM eclipse-temurin:17-jdk-alpine as builder

WORKDIR /app

# 1. Chỉ copy các file build
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# 2. Download dependencies trước (cache layer này)
RUN ./gradlew dependencies --no-daemon --info || true

# 3. Copy source code (rebuild khi code thay đổi)
COPY src ./src

# 4. Build với verbose output để debug
RUN ./gradlew bootJar --no-daemon --info --stacktrace -x test

# ----- Stage 2: Runtime (Giữ nguyên, đã rất tốt) -----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]