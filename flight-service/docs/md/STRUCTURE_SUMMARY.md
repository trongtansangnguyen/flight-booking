# Cấu trúc Clean Architecture - Flight Service

## ✅ Đã dọn dẹp xong! Không còn duplicate files.

## Cấu trúc thư mục hiện tại:

```
src/main/java/org/example/flight/
│
├── FlightServiceApplication.java          ← Main Spring Boot Application
│
├── domain/                                 ← 💎 DOMAIN LAYER (No dependencies)
│   ├── entity/
│   │   └── Aircraft.java                  ← Pure domain entity
│   ├── exception/
│   │   └── AircraftNotFoundException.java ← Domain exception
│   └── valueobject/                       ← (Empty - for future value objects)
│
├── application/                            ← ⚙️ APPLICATION LAYER (Depends on Domain only)
│   ├── port/
│   │   ├── input/
│   │   │   └── AircraftUseCase.java       ← Input Port (Use Case Interface)
│   │   └── output/
│   │       └── AircraftRepositoryPort.java ← Output Port (Repository Interface)
│   ├── usecase/
│   │   └── AircraftUseCaseImpl.java       ← Business Logic Implementation
│   ├── dto/
│   │   ├── AircraftRequest.java           ← Request DTO
│   │   └── AircraftResponse.java          ← Response DTO
│   └── mapper/                            ← (Empty - for future mappers)
│
└── infrastructure/                         ← 🏗️ INFRASTRUCTURE LAYER (Depends on Application & Domain)
    ├── adapter/
    │   ├── input/
    │   │   └── rest/
    │   │       ├── AircraftController.java        ← REST Controller (Input Adapter)
    │   │       └── exception/
    │   │           └── GlobalExceptionHandler.java ← Exception Handler
    │   └── output/
    │       ├── persistence/
    │       │   ├── entity/
    │       │   │   └── AircraftJpaEntity.java     ← JPA Entity
    │       │   ├── repository/
    │       │   │   └── AircraftJpaRepository.java ← Spring Data JPA Repository
    │       │   └── adapter/
    │       │       └── AircraftRepositoryAdapter.java ← Repository Adapter (implements Output Port)
    │       └── messaging/                 ← (Empty - for future Kafka adapters)
    └── config/
        └── AircraftBeanConfiguration.java ← 🔧 CONTAINER: Dependency Injection Configuration
```

## Các file đã XÓA (duplicate):

❌ `controller/AircraftController.java`
❌ `dto/AircraftRequest.java`
❌ `dto/AircraftResponse.java`
❌ `model/Aircraft.java`
❌ `repository/AircraftRepository.java`
❌ `service/AircraftService.java`
❌ `service/AircraftServiceImpl.java`
❌ `exception/AircraftNotFoundException.java`
❌ `exception/GlobalExceptionHandler.java`

## Tổng số files: 14 files

### Domain Layer (2 files):
- Aircraft.java
- AircraftNotFoundException.java

### Application Layer (5 files):
- AircraftUseCase.java (Input Port)
- AircraftRepositoryPort.java (Output Port)
- AircraftUseCaseImpl.java (Use Case Implementation)
- AircraftRequest.java (DTO)
- AircraftResponse.java (DTO)

### Infrastructure Layer (6 files):
- AircraftController.java (REST Input Adapter)
- GlobalExceptionHandler.java
- AircraftJpaEntity.java (JPA Entity)
- AircraftJpaRepository.java (Spring Data)
- AircraftRepositoryAdapter.java (Output Adapter)
- AircraftBeanConfiguration.java (DI Container)

### Root (1 file):
- FlightServiceApplication.java (Main)

## Dependency Flow (tuân thủ Dependency Rule):

```
Container (config)
    ↓ depends on
Infrastructure (adapters, repositories, controllers)
    ↓ depends on
Application (use cases, ports, DTOs)
    ↓ depends on
Domain (entities, exceptions)
    ↓ depends on
NOTHING! ✅
```

## Build Status: ✅ SUCCESS

```bash
./gradlew clean compileJava
# BUILD SUCCESSFUL
```

## Các entities khác cần refactor tương tự:

Bạn có thể áp dụng pattern này cho:
- 🛫 Flight
- 🎫 Ticket  
- 🏢 Airport

Mỗi entity sẽ có cùng cấu trúc:
- Domain entity (pure)
- Application use case + ports
- Infrastructure adapters (REST, JPA)
- Configuration bean
