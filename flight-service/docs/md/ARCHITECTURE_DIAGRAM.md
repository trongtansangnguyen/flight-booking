# Clean Architecture - Dependency Diagram

## Package Structure
```
src/main/java/org/example/flight/
│
├── domain/                          ← DOMAIN LAYER (No dependencies)
│   ├── entity/
│   │   └── Aircraft.java           ← Pure business entity
│   ├── exception/
│   │   └── AircraftNotFoundException.java
│   └── valueobject/
│
├── application/                     ← APPLICATION LAYER (Depends on Domain only)
│   ├── port/
│   │   ├── input/
│   │   │   └── AircraftUseCase.java        ← Input Port (interface)
│   │   └── output/
│   │       └── AircraftRepositoryPort.java ← Output Port (interface)
│   ├── usecase/
│   │   └── AircraftUseCaseImpl.java        ← Business logic implementation
│   ├── dto/
│   │   ├── AircraftRequest.java
│   │   └── AircraftResponse.java
│   └── mapper/
│
└── infrastructure/                  ← INFRASTRUCTURE LAYER (Depends on Application & Domain)
    ├── adapter/
    │   ├── input/
    │   │   └── rest/
    │   │       ├── AircraftController.java          ← REST Controller
    │   │       └── exception/
    │   │           └── GlobalExceptionHandler.java
    │   └── output/
    │       ├── persistence/
    │       │   ├── entity/
    │       │   │   └── AircraftJpaEntity.java       ← JPA Entity
    │       │   ├── repository/
    │       │   │   └── AircraftJpaRepository.java   ← Spring Data JPA
    │       │   └── adapter/
    │       │       └── AircraftRepositoryAdapter.java ← Implements Output Port
    │       └── messaging/
    │           └── (Kafka producers/consumers)
    └── config/
        └── AircraftBeanConfiguration.java   ← CONTAINER: Wire everything
```

## Dependency Flow

```mermaid
graph TD
    subgraph Container["🔧 CONTAINER LAYER"]
        Config[AircraftBeanConfiguration]
    end
    
    subgraph Infrastructure["🏗️ INFRASTRUCTURE LAYER"]
        Controller[AircraftController<br/>REST API]
        ExHandler[GlobalExceptionHandler]
        JpaEntity[AircraftJpaEntity<br/>@Entity]
        JpaRepo[AircraftJpaRepository<br/>Spring Data]
        RepoAdapter[AircraftRepositoryAdapter<br/>Implements Port]
    end
    
    subgraph Application["⚙️ APPLICATION LAYER"]
        UseCase[AircraftUseCaseImpl<br/>Business Logic]
        InputPort[AircraftUseCase<br/>Interface]
        OutputPort[AircraftRepositoryPort<br/>Interface]
        DTO[DTOs: Request/Response]
    end
    
    subgraph Domain["💎 DOMAIN LAYER"]
        Entity[Aircraft<br/>Pure Entity]
        Exception[AircraftNotFoundException]
    end
    
    %% Dependencies (arrows point TO dependencies)
    Config --> Controller
    Config --> UseCase
    Config --> RepoAdapter
    Config --> JpaRepo
    
    Controller --> InputPort
    Controller --> DTO
    ExHandler --> Exception
    
    RepoAdapter --> OutputPort
    RepoAdapter --> Entity
    RepoAdapter --> JpaEntity
    RepoAdapter --> JpaRepo
    
    UseCase --> InputPort
    UseCase --> OutputPort
    UseCase --> Entity
    UseCase --> Exception
    UseCase --> DTO
    
    %% Domain has NO dependencies
    
    style Domain fill:#90EE90
    style Application fill:#87CEEB
    style Infrastructure fill:#FFB6C1
    style Container fill:#FFD700
```

## Dependency Rules Visualization

```
┌────────────────────────────────────────────────┐
│           ALLOWED DEPENDENCIES                  │
├────────────────────────────────────────────────┤
│                                                 │
│  Container        →  All Layers                │
│                                                 │
│  Infrastructure   →  Application, Domain       │
│                                                 │
│  Application      →  Domain                    │
│                                                 │
│  Domain           →  NOTHING!                  │
│                                                 │
└────────────────────────────────────────────────┘
```

## Request Flow

```
┌──────────┐
│  Client  │
└────┬─────┘
     │ HTTP POST /api/aircrafts
     ↓
┌────────────────────────────────────────────────┐
│ INFRASTRUCTURE - Input Adapter                 │
│ AircraftController.createAircraft()            │
│ - Nhận HTTP request                            │
│ - Validate input                               │
└────┬───────────────────────────────────────────┘
     │ aircraftUseCase.createAircraft(request)
     ↓
┌────────────────────────────────────────────────┐
│ APPLICATION - Use Case                         │
│ AircraftUseCaseImpl.createAircraft()           │
│ - Business logic                               │
│ - Aircraft.create(model, name, capacity)       │
└────┬───────────────────────────────────────────┘
     │ Aircraft domain entity
     ↓
┌────────────────────────────────────────────────┐
│ DOMAIN - Entity                                │
│ Aircraft (Pure business object)                │
│ - Validate business rules                      │
│ - No framework dependencies                    │
└────┬───────────────────────────────────────────┘
     │ repositoryPort.save(aircraft)
     ↓
┌────────────────────────────────────────────────┐
│ APPLICATION - Output Port                      │
│ AircraftRepositoryPort (Interface)             │
│ - Định nghĩa contract                          │
└────┬───────────────────────────────────────────┘
     │ Implementation
     ↓
┌────────────────────────────────────────────────┐
│ INFRASTRUCTURE - Output Adapter                │
│ AircraftRepositoryAdapter                      │
│ - Map Domain Entity → JPA Entity               │
│ - jpaRepository.save(jpaEntity)                │
└────┬───────────────────────────────────────────┘
     │
     ↓
┌────────────────────────────────────────────────┐
│ INFRASTRUCTURE - JPA Repository                │
│ AircraftJpaRepository (Spring Data)            │
│ - Save to database                             │
└────────────────────────────────────────────────┘
```

## Hexagonal Architecture View

```
              ┌─────────────────────────────┐
              │   PRIMARY ADAPTERS          │
              │   (Driving/Input)           │
              │                             │
              │  ┌─────────────────────┐    │
              │  │ REST Controller     │    │
              │  │ (HTTP)              │    │
              │  └──────────┬──────────┘    │
              └─────────────┼───────────────┘
                            │
                            ↓
         ┌──────────────────────────────────────┐
         │        APPLICATION CORE               │
         │                                       │
         │  ┌────────────────────────────────┐  │
         │  │  Input Ports (Interfaces)      │  │
         │  │  - AircraftUseCase             │  │
         │  └────────────┬───────────────────┘  │
         │               ↓                       │
         │  ┌────────────────────────────────┐  │
         │  │  Use Cases                     │  │
         │  │  - Business Logic              │  │
         │  └────────────┬───────────────────┘  │
         │               ↓                       │
         │  ┌────────────────────────────────┐  │
         │  │  Domain Entities               │  │
         │  │  - Aircraft (Pure)             │  │
         │  └────────────┬───────────────────┘  │
         │               ↓                       │
         │  ┌────────────────────────────────┐  │
         │  │  Output Ports (Interfaces)     │  │
         │  │  - AircraftRepositoryPort      │  │
         │  └────────────┬───────────────────┘  │
         └───────────────┼───────────────────────┘
                         │
                         ↓
         ┌───────────────────────────────────────┐
         │   SECONDARY ADAPTERS                  │
         │   (Driven/Output)                     │
         │                                       │
         │  ┌──────────────┐  ┌──────────────┐  │
         │  │ Repository   │  │   Kafka      │  │
         │  │ Adapter      │  │   Producer   │  │
         │  │ (Database)   │  │              │  │
         │  └──────────────┘  └──────────────┘  │
         └───────────────────────────────────────┘
```
