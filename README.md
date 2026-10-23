# Lab 3: Fault Tolerance Microservices

**(Hotels Booking System)

## Project Overview
The system consists of four independent services (Gateway, Loyalty, Payment, Reservation) communicating via HTTP/REST, orchestrated by a single Gateway.

The core objective of this lab is to ensure system resilience. If a non-critical service becomes unavailable, 
the system does not crash; instead, it degrades gracefully using **Circuit Breakers**, **Fallbacks**, and an **Asynchronous Retry Queue**.

## Architecture & Fault Tolerance Features

| Service                 | Port   | Description                                                             | Database       |
| :---------------------- | :----- | :---------------------------------------------------------------------- | :------------- |
| **Gateway Service**     | `8080` | Orchestrator. Handles Circuit Breaking, Fallbacks, and the Retry Queue. | None           |
| **Loyalty Service**     | `8050` | Manages user statuses and discounts.                                    | `balances`     |
| **Payment Service**     | `8060` | Handles payments.                                                       | `payments`     |
| **Reservation Service** | `8070` | Manages hotels and reservations.                                        | `reservations` |

### Implemented Mechanisms:
1. **Circuit Breaker (Resilience4j)**: Applied to all read operations in the Gateway. If a service fails `N` times, the circuit opens, and requests immediately return a fallback without waiting for a timeout. After a short duration, the circuit allows test requests (Half-Open state) to check for recovery.
2. **Fallbacks**: 
   - If **Loyalty** is down, the system returns default values (`status: UNKNOWN`, `discount: 0`).
   - If **Reservation** (hotels list) is down, it returns an empty list.
3. **Rollback & Queue**: For complex state-changing operations (e.g., Booking a Hotel):
   - If the operation fails halfway (e.g., Payment succeeded but Loyalty is down), the system performs a **Rollback** (cancels the payment and reservation).
   - It returns a **Fake Success** to the user.
   - The failed operation is placed in an in-memory `BlockingQueue`.
   - A background worker thread constantly retries the operation until it succeeds.
  
## Technologies Used
- **Java 17** (Eclipse Temurin)
- **Spring Boot 3.2.5** (Web, WebFlux)
- **Resilience4j** (Circuit Breaker, Retry patterns)
- **PostgreSQL 13**
- **Docker & Docker Compose**
- **Newman & Postman** (Automated Integration Testing)
- **GitHub Actions** (CI/CD)
