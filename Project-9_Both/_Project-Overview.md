# Project Overview

## Project Layout / Steps

### Github Repositories

1) **tsg-9.27-{teamName}-frontend-feedback-ui** (React)
2) **tsg-9.27-{teamName}-feedback-api** (Spring Boot App 1: REST API → Postgres → Kafka producer)
3) **tsg-9.27-{teamName}-feedback-analytics-consumer** (Spring Boot App 2: Kafka consumer → simulates analytics logging)

> Each repo must be standalone, Dockerized, with clear README and npm scripts to run locally. Provide a top-level `docker-compose.yaml` in **one** repo that brings up **all** services (UI, both Spring apps, Postgres, Kafka/{Kraft|ZooKeeper}).

### High-Level Architecture

- **React UI** → calls **feedback-api** REST endpoints.
- **feedback-api**:
  - Validates request (service layer).
  - Persists Feedback to **Postgres** (repo layer).
  - Publishes a compact event to Kafka topic **`feedback-submitted`**.
- **feedback-analytics-consumer**:
  - Subscribes to **`feedback-submitted`** topic.
  - Simulates analytics by structured logging.
  - Use the kafka dashboard UI to view the raw events in the topic.

### Domain: Feedback

**Feedback fields (DB & DTOs)**
- `id` (UUID generated server-side)
- `memberId` (string; non-empty; max 36)
- `providerName` (string; non-empty; max 80)
- `rating` (integer 1–5)
- `comment` (string; optional; **max 200**)
- `submittedAt` (server timestamp, UTC/ISO 8601)

### API (feedback-api)

**Base URL:** `/api/v1`

#### Endpoints
- `POST /feedback`
  - Request (JSON):
    ```json
    {
      "memberId": "m-123",
      "providerName": "Dr. Smith",
      "rating": 4,
      "comment": "Great experience."
    }
    ```
  - Responses:
    - **201** with created resource (including `id`, `submittedAt`).
    - **400** on validation errors (see Validation Rules).

- `GET /feedback/{id}`
  - **200** with resource; **404** if not found.

- `GET /feedback?memberId=<id>`
  - **200** list (may be empty). Pagination optional.

- `GET /health`  
  - **200** simple health check.

> Controllers must be **thin**: mapping, request/response handling only. All business rules in **services**; DB in **repositories**; transport models in **dtos**.

### Kafka Contract

**Topic:** `feedback-submitted`  
**Key:** `id` (UUID as string)  
**Value:** JSON payload (stable event contract)

```json
{
  "id": "5f1a4d9e-...-b2",
  "memberId": "m-123",
  "providerName": "Dr. Smith",
  "rating": 4,
  "comment": "Great experience.",
  "submittedAt": "2025-11-10T20:23:00Z",
  "schemaVersion": 1
}
```

- **Producer:** feedback-api publishes **after** DB commit. At-least-once is sufficient for this exercise.
- **Consumer:** feedback-analytics-consumer logs: `Received feedback (id=...) rating=... provider=... member=...`

### Validation Rules (service layer)

- `memberId`: required, non-empty, length ≤ 36
- `providerName`: required, non-empty, length ≤ 80
- `rating`: required, integer 1–5
- `comment`: optional, length ≤ **200**
- Reject unknown fields in DTOs (configure Jackson).
- Return **400** with a machine-readable error body, e.g.:
  ```json
  {
    "errors": [
      {"field": "comment", "message": "Must be ≤ 200 characters"}
    ]
  }
  ```

### Data Model (feedback-api → Postgres)

**Table: `feedback`**
```sql
id UUID PRIMARY KEY,
member_id VARCHAR(36) NOT NULL,
provider_name VARCHAR(80) NOT NULL,
rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
comment VARCHAR(200),
submitted_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
```

### Required Project Structure (feedback-api)

```
src/main/java/com/tsg/feedbackapi/
  controllers/
    FeedbackController.java
  services/
    FeedbackService.java
    ValidationException.java
  repositories/
    FeedbackRepository.java  (Spring Data JPA)
    entities/FeedbackEntity.java
  dtos/
    FeedbackRequest.java
    FeedbackResponse.java
    ErrorResponse.java
  messaging/
    KafkaProducerConfig.java
    FeedbackEventPublisher.java
```

**Controller responsibilities**
- Map endpoints, parse DTOs, return responses.
- No business logic.

**Service responsibilities**
- Validate inputs per rules above.
- Map DTOs ↔ entities.
- Persist via repository.
- Publish Kafka event.

**Repository responsibilities**
- Data access only (Spring Data JPA).

### Consumer App Structure (feedback-analytics-consumer)

```
src/main/java/com/tsg/feedbackconsumer/
  messaging/
    KafkaConsumerConfig.java
    FeedbackEventListener.java
  controllers/
    HealthController.java
```
- Listener deserializes event value into a POJO and logs structured JSON.

### React UI (frontend-feedback-ui)

- Pages/Components:
  - **Submit Feedback** form (memberId, providerName, rating [1–5], comment).
  - **My Feedback** list filtered by memberId (simple input + list).
  - Basic validation mirrors server rules (length, rating range).
- Show clear success/error states from API responses.

### Docker & Local Infra

- **Postgres** (expose 5432) with init user/db.
- **Kafka** with a single broker for simplicity.
- **docker-compose.yaml** brings up:
  - postgres
  - kafka (kraft or zookeeper is fine)
  - feedback-api
  - feedback-analytics-consumer
  - frontend-feedback-ui
- health checks and sensible `depends_on`.
- One-command local up (e.g., `docker compose up -d`) and per-service logs.

### Testing Requirements

**feedback-api**
- **Unit tests** (JUnit 5 + Mockito):
  - All logic must be unit tested
- **Repository test** (If time allows) Integration tests using Testcontainers.
- **Controller test** (MockMvc) for happy path and validation error body.

**feedback-analytics-consumer**
- **Unit tests** for listener: valid message and bad payload handling.
- Optional integration test with embedded Kafka or Testcontainers.

**React**
- Light tests for form validation (e.g., React Testing Library) are a plus but optional.

> Aim for meaningful tests over raw coverage numbers.

## Deliverables

- 3 Git repos with:
  - Source code
  - **README** (how to run locally with Docker, env vars, sample requests)
  - OpenAPI/Swagger (YAML or auto-generated UI at `/swagger-ui`)
  - Test instructions (`./mvn test`, `npm test`)

## Acceptance Criteria (pass/fail)

1. React UI can submit feedback and list feedback by `memberId`.
2. feedback-api enforces validation (incl. **200-char comment max**).
3. feedback-api persists to Postgres and publishes to Kafka on success.
4. feedback-analytics-consumer receives and logs events reliably.
5. All three repos run together via Docker Compose with minimal setup.
6. Unit tests present and meaningful for service/consumer logic; tests pass.
7. Clean layering: controllers thin, services contain business logic, repositories for data access, DTOs separate.
8. Clear, concise documentation and simple run instructions.
