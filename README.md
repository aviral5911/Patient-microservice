Patient Management System - complete architecture

A microservices-based backend system for managing patient records, billing, and analytics — built to explore real-world distributed systems patterns: service isolation, synchronous vs. asynchronous communication, centralized auth, and cloud-native deployment.

## Architecture

The system is composed of 5 independently deployable Spring Boot services:

| Service | Responsibility |
|---|---|
| **Patient Service**       | Manages patient records and profile data |
| **Billing Service**       | Handles billing operations, called synchronously via gRPC |
| **Analytics Service**     | Consumes patient events asynchronously via Kafka for reporting/insights |
| **Notification Service**  | Consumes events via Kafka to trigger notifications |
| **Auth Service**          | Issues and manages JWT tokens for all services |

All external traffic enters through a single **API Gateway** (Spring Cloud Gateway), which validates JWTs centrally before routing requests — no downstream service duplicates auth logic, and no service is directly reachable from outside the Docker network.

```
                          ┌─────────────────┐
 Client ─────────────────▶│   API Gateway    │──▶ JWT validation (WebFlux filter)
                          └────────┬─────────┘
                                   │
        ┌──────────────┬──────────┼──────────────┬───────────────┐
        ▼              ▼          ▼              ▼               ▼
   Auth Service   Patient Service │          Notification     Analytics
                        │  gRPC   ▼            Service          Service
                        └────▶ Billing            ▲                ▲
                                Service            │                │
                                          Kafka (async events) ─────┘
```

## Why these design choices

- **gRPC between Patient ↔ Billing**: synchronous, type-safe communication where an immediate response is required (e.g., generating a bill on patient creation).
- **Kafka for Analytics/Notification**: asynchronous, event-driven — these services don't need to block the main request path, and can scale/fail independently of Patient Service.
- **JWT validation at the Gateway, not per-service**: avoids duplicating auth logic across 5 services; a single reactive filter handles it once for all downstream traffic.
- **Docker network isolation**: every service is on a shared internal Docker network; only the API Gateway is exposed externally.

## Tech Stack

**Backend:** Java, Spring Boot, Spring Security, Spring Cloud Gateway, Spring Data JPA/Hibernate
**Communication:** gRPC (Protocol Buffers), Apache Kafka
**Database:** PostgreSQL
**Auth:** JWT
**Testing:** JUnit, Spring Boot Test (integration tests for auth flows and patient endpoints)
**Infra/Cloud:** Docker, Docker Compose, AWS (CloudFormation, ECS, RDS, MSK, Application Load Balancer), LocalStack (for local AWS simulation)



> API Gateway will be available at `http://localhost:4004` 

### Prerequisites
- Docker & Docker Compose
- Java 17+ (if running services outside Docker)
- Maven



Integration tests cover authenticated/unauthenticated request flows and JWT validation at the gateway using Spring Boot Test.


## Project Structure

```
patient-microservice/
├── api-gateway/
├── auth-service/
├── patient-service/
├── billing-service/
├── analytics-service/
├── notification-service/
```

## Author

**Aviral Choudhary**
[LinkedIn](https://www.linkedin.com/in/aviral-choudhary-68b7ba291/) · [GitHub](https://github.com/aviral5911)
