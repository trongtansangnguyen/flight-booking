# ----- Stage 1: Build (Tối ưu hóa) -----
FROM eclipse-temurin:17-jdk-alpine as builder

WORKDIR /app

# 1. Chỉ copy các file build
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# 2. TÁCH BIỆT LAYER DEPENDENCIES
# Chỉ tải các thư viện. Layer này sẽ được cache
# miễn là file build.gradle không thay đổi.
RUN ./gradlew dependencies --no-daemon

# 3. BÂY GIỜ MỚI COPY SOURCE CODE
# Khi bạn đổi code, chỉ layer này và layer 'bootJar'
# phải chạy lại. Layer 'dependencies' ở trên vẫn được cache.
COPY src ./src

# 4. Build ứng dụng
# Lệnh này giờ sẽ chạy SIÊU NHANH vì thư viện đã có sẵn
# Nó chỉ cần compile code 'src' của bạn
RUN ./gradlew bootJar --no-daemon -x test

# ----- Stage 2: Runtime (Giữ nguyên, đã rất tốt) -----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]