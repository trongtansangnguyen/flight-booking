# Clean Architecture - Flight Service

## Cấu trúc theo Dependency Rule

```
┌─────────────────────────────────────────────────────────────────┐
│                        CONTAINER LAYER                           │
│                   (Spring Configuration)                         │
│  - AircraftBeanConfiguration                                     │
│  - Wiring all dependencies                                       │
│  Dependencies: ↓ ALL LAYERS                                      │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                          │
│                                                                   │
│  Input Adapters (Controllers):                                  │
│  - AircraftController                                            │
│  - GlobalExceptionHandler                                        │
│                                                                   │
│  Output Adapters (Persistence):                                 │
│  - AircraftJpaEntity                                             │
│  - AircraftJpaRepository                                         │
│  - AircraftRepositoryAdapter                                     │
│                                                                   │
│  Dependencies: ↓ Application Layer, ↓ Domain Layer              │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                     APPLICATION LAYER                            │
│                                                                   │
│  Use Cases:                                                      │
│  - AircraftUseCaseImpl                                           │
│                                                                   │
│  Ports (Interfaces):                                            │
│  - AircraftUseCase (Input Port)                                  │
│  - AircraftRepositoryPort (Output Port)                          │
│                                                                   │
│  DTOs:                                                           │
│  - AircraftRequest                                               │
│  - AircraftResponse                                              │
│                                                                   │
│  Dependencies: ↓ Domain Layer ONLY                               │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                       DOMAIN LAYER                               │
│                     (Core Business Logic)                        │
│                                                                   │
│  Entities:                                                       │
│  - Aircraft (Pure domain entity, no framework)                   │
│                                                                   │
│  Exceptions:                                                     │
│  - AircraftNotFoundException                                     │
│                                                                   │
│  Dependencies: NONE (Không phụ thuộc gì!)                        │
└─────────────────────────────────────────────────────────────────┘
```

## DEPENDENCY RULE - Tất cả dependencies chỉ trỏ vào trong

### 1. **Domain Layer** (org.example.flight.domain)
- ✅ **Không phụ thuộc vào bất kỳ layer nào khác**
- ✅ Không có Spring, JPA, hoặc framework annotations
- ✅ Pure Java - Business logic thuần túy
- Chứa:
  - Entities (Aircraft)
  - Exceptions (AircraftNotFoundException)
  - Value Objects (nếu có)

### 2. **Application Layer** (org.example.flight.application)
- ✅ **Phụ thuộc vào Domain Layer ONLY**
- ✅ Không phụ thuộc Infrastructure
- ✅ Không biết về Spring, JPA, REST
- Chứa:
  - Use Cases (AircraftUseCaseImpl)
  - Input Ports (AircraftUseCase interface)
  - Output Ports (AircraftRepositoryPort interface)
  - DTOs (AircraftRequest, AircraftResponse)

### 3. **Infrastructure Layer** (org.example.flight.infrastructure)
- ✅ **Phụ thuộc vào Domain và Application**
- ✅ Implement các Output Ports từ Application
- ✅ Có thể dùng Spring, JPA, và framework khác
- Chứa:
  - **Input Adapters**: Controllers, REST endpoints
  - **Output Adapters**: JPA repositories, adapters
  - **Persistence**: JPA entities, mappers
  - **Configuration**: Exception handlers

### 4. **Container Layer** (org.example.flight.infrastructure.config)
- ✅ **Phụ thuộc vào TẤT CẢ các layers**
- ✅ Wiring dependencies với Spring @Bean
- ✅ Nơi DUY NHẤT biết về tất cả implementations
- Chứa:
  - Spring Configuration classes
  - Bean definitions

## Lợi ích của kiến trúc này

### 1. **Testability**
```java
// Test Use Case không cần Spring hay Database
AircraftRepositoryPort mockRepo = mock(AircraftRepositoryPort.class);
AircraftUseCase useCase = new AircraftUseCaseImpl(mockRepo);
```

### 2. **Maintainability**
- Thay đổi database (PostgreSQL → MongoDB): Chỉ sửa Infrastructure Layer
- Thay đổi API (REST → GraphQL): Chỉ sửa Input Adapter
- Business logic giữ nguyên!

### 3. **Independence**
- Domain không biết về Spring
- Application không biết về JPA
- Có thể chạy use case mà không cần framework

### 4. **Dependency Inversion**
```
Application định nghĩa interface (Output Port)
     ↑
     |
Infrastructure implement interface đó
```

## Kiểm tra Dependency Rule

### ✅ Domain Layer
```bash
# Domain KHÔNG được import:
❌ org.springframework.*
❌ jakarta.persistence.*
❌ org.example.flight.application.*
❌ org.example.flight.infrastructure.*
```

### ✅ Application Layer
```bash
# Application CHỈ được import Domain:
✅ org.example.flight.domain.*
❌ org.springframework.*
❌ jakarta.persistence.*
❌ org.example.flight.infrastructure.*
```

### ✅ Infrastructure Layer
```bash
# Infrastructure có thể import:
✅ org.example.flight.domain.*
✅ org.example.flight.application.*
✅ org.springframework.*
✅ jakarta.persistence.*
```

## Flow của một request

```
1. HTTP Request → AircraftController (Infrastructure - Input Adapter)
                      ↓
2. Controller gọi → AircraftUseCase (Application - Input Port)
                      ↓
3. UseCase thực hiện business logic với Domain Entity
                      ↓
4. UseCase gọi → AircraftRepositoryPort (Application - Output Port)
                      ↓
5. AircraftRepositoryAdapter (Infrastructure - Output Adapter) implement port
                      ↓
6. Adapter dùng AircraftJpaRepository để lưu/lấy data
                      ↓
7. Adapter map JpaEntity ↔ Domain Entity
                      ↓
8. Response trở về qua các layers
```

## Hexagonal Architecture (Ports & Adapters)

```
        Input Ports              Output Ports
             ↓                        ↑
    [AircraftController]    [AircraftRepositoryAdapter]
             ↓                        ↑
        ┌─────────────────────────────────┐
        │   Application Core              │
        │   (Use Cases + Domain)          │
        │   - AircraftUseCaseImpl         │
        │   - Aircraft Entity             │
        └─────────────────────────────────┘
```

## Microservice với Clean Architecture

Mỗi service (flight, order, payment) nên có cấu trúc tương tự:
- **Domain**: Business entities và rules
- **Application**: Use cases và ports
- **Infrastructure**: Adapters (REST, Kafka, Database)
- **Container**: Spring configuration
