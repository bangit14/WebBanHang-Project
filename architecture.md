# System Architecture

> This document is completed **after** [Analysis and Design](analysis-and-design.md).
> Based on the Service Candidates and Non-Functional Requirements identified there, select appropriate architecture patterns and design the deployment architecture.

**References:**

1. _Service-Oriented Architecture: Analysis and Design for Services and Microservices_ — Thomas Erl (2nd Edition)
2. _Microservices Patterns: With Examples in Java_ — Chris Richardson
3. _Bài tập — Phát triển phần mềm hướng dịch vụ_ — Hung Dang (available in Vietnamese)

---

## 1. Pattern Selection

Select patterns based on business/technical justifications from your analysis.

| Pattern                      | Selected?     | Business/Technical Justification                                                          |
| ---------------------------- | ------------- | ----------------------------------------------------------------------------------------- |
| API Gateway                  | ✅            | Central entry point cho client, routing request đến Booking (và read APIs), auth, logging |
| Database per Service         | ✅            | Mỗi service có DB riêng → loose coupling, đúng DDD                                        |
| Shared Database              | ❌            | Gây coupling, không phù hợp microservices                                                 |
| Saga (Choreography)          | ✅            | Transaction phân tán thông qua event chain (không orchestration)                          |
| Event-driven / Message Queue | ✅            | Giao tiếp async qua Kafka/RabbitMQ                                                        |
| CQRS                         | ⚠️ (Optional) | Tách read/write (Room có thể expose API read)                                             |
| Circuit Breaker              | ⚠️            | Ít dùng hơn do async, nhưng vẫn cần cho Gateway/External calls                            |
| Service Registry / Discovery | ✅            | Dynamic discovery (Eureka)                                                                |
| Other: Outbox Pattern        | ✅            | Đảm bảo atomicity giữa DB và event publish                                                |

---

## 2. System Components

| Component            | Responsibility                          | Tech Stack           | Port |
| -------------------- | --------------------------------------- | -------------------- | ---- |
| Frontend             | UI đặt phòng                            | ReactJS              | 3000 |
| Gateway              | Routing + Auth                          | Spring Cloud Gateway | 8080 |
| Booking Service      | Booking lifecycle + publish event       | Spring Boot          | 8081 |
| Payment Service      | Consume BookingCreated + xử lý payment  | Spring Boot          | 8082 |
| Room Service         | Consume BookingConfirmed + reserve room | Spring Boot          | 8083 |
| Notification Service | Consume BookingConfirmed + gửi email    | Spring Boot          | 8084 |
| Message Broker       | Event streaming                         | Kafka / RabbitMQ     | 9092 |
| Service Registry     | Service discovery                       | Eureka               | 8761 |
| Booking DB           | Lưu booking                             | PostgreSQL           | 5432 |
| Payment DB           | Lưu payment                             | PostgreSQL           | 5433 |
| Room DB              | Lưu room                                | PostgreSQL           | 5434 |
| Notification DB      | Lưu log email                           | PostgreSQL           | 5435 |

---

### 3. Communication

Inter-service Communication Matrix

| From → To            | Booking Service                 | Payment Service        | Room Service             | Notification Service     | Gateway | Database |
| -------------------- | ------------------------------- | ---------------------- | ------------------------ | ------------------------ | ------- | -------- |
| Frontend             | ❌                              | ❌                     | ❌                       | ❌                       | ✅      | ❌       |
| Gateway              | REST (read APIs only)           | ❌                     | REST (read)              | ❌                       | ❌      | ❌       |
| Booking Service      | ❌                              | Event (BookingCreated) | Event (BookingConfirmed) | Event (BookingConfirmed) | ❌      | JDBC     |
| Payment Service      | Event (PaymentSucceeded/Failed) | ❌                     | ❌                       | ❌                       | ❌      | JDBC     |
| Room Service         | ❌                              | ❌                     | ❌                       | ❌                       | ❌      | JDBC     |
| Notification Service | ❌                              | ❌                     | ❌                       | ❌                       | ❌      | JDBC     |


| Event Name       | Producer | Consumer           |
| ---------------- | -------- | ------------------ |
| BookingCreated   | Booking  | Payment            |
| PaymentSucceeded | Payment  | Booking            |
| PaymentFailed    | Payment  | Booking            |
| BookingConfirmed | Booking  | Room, Notification |

---

## 4. Architecture Diagram

> Place diagrams in `docs/asset/` and reference here.

```mermaid
graph LR
    U[User] --> FE[Frontend]
    FE --> GW[API Gateway]

    GW --> BS[Booking Service]

    BS --> DB1[(Booking DB)]
    PS --> DB2[(Payment DB)]
    RS --> DB3[(Room DB)]
    NS --> DB4[(Notification DB)]

    BS --> MQ[(Message Broker)]

    MQ --> PS[Payment Service]
    PS --> MQ

    MQ --> BS
    BS --> MQ

    MQ --> RS[Room Service]
    MQ --> NS[Notification Service]
```

---

## 5. Deployment

- All services containerized with Docker
- Orchestrated via Docker Compose
- Single command: `docker compose up --build`
