# Provider Feedback Portal - Feedback API (Spring Boot + Postgres + Kafka)

**THE PROJECT MAY NEED CERTAIN PATHS UPDATED AS THE NAME AND PATH WAS UPDATED -- CODE MAY NOT RUN UNTIL UPDATED**

The **Feedback API** is the backend service for the Provider Feedback Portal.  
It receives feedback submissions, stores them in **Postgres**, and publishes **Kafka events** that the analytics consumer service processes.

This service is designed to run **together with**:
- **[Frontend Feedback UI](https://github.com/York-Solutions-B2E/tsg-9.27-TheAvengers-frontend-feedback-ui)**
- **[Feedback Analytics Consumer](https://github.com/York-Solutions-B2E/tsg-9.27-TheAvengers-feedback-analytics-consumer)**
- **Postgres + Kafka** (started automatically via Docker Compose)

This API **cannot run in isolation** unless Postgres and Kafka are manually provided.

## How to Run the Application

#### Required Project Layout
To run this system with Docker Compose, the **frontend**, **API**, and **consumer** applications must all be placed in the **same parent folder**.

Example:
```
project-root/
 ├── tsg-9.27-TheAvengers-frontend-feedback-ui/
 ├── tsg-9.27-TheAvengers-feedback-api/
 ├── tsg-9.27-TheAvengers-feedback-analytics-consumer/
 └── docker-compose.yml

```
Use the **docker-compose.yml** file included in **this repository**.  
It builds and runs:

- The Feedback API
- Postgres
- Kafka
- Kafka UI tools

From the root of this repository:
```
docker compose up --build
```
This will automatically provide:

- **Postgres database** (service name: `db`, internal port: `5432`)
- **Kafka broker** (internal port: `9092`)
- **Network connectivity** between all services

The API container connects to Postgres using:
```
jdbc:postgresql://db:5432/final_project
```
The API listens on:
```
http://localhost:8080/api/v1
```

#### Features

- **POST /feedback** — submit feedback
- **GET /feedback/{id}** — fetch feedback by UUID
- **GET /feedback?memberId=...** — fetch feedback for a member
- Stores feedback in **Postgres**
- Publishes `feedback-submitted` events to **Kafka**
- Uses **DTOs**, **validation**, and **exception handling**

#### Tech Stack

- **Java 17+**

- **Spring Boot**

- **Spring Web**

- **Spring Data JPA**

- **Postgres**

- **Kafka (Producer)**

- **Docker & Docker Compose**

#### Running Tests
You can still run backend tests locally without Docker:
```
mvn test
```

In order to run Unit-Tests on the *FeedbackControllerTests.java*, please take the following steps:

1. In IntelliJ -> Click Run (Toolbar)
2. Click Edit Configurations
3. Select the *FeedbackControllerTests* configuration (one may need to be created if not there)
4. Under Build and Run, in the VM Options input field, add `-Dnet.bytebuddy.experimental=true`
5. Click Apply
6. Click Run

This is necessary due to compatibility issues with JDK 25 and Byte Buddy (a Mockito requirement).

## Dependencies on Other Apps

This application **depends on**:

#### 1. **Postgres**
Required for storing feedback.
#### 2. **Kafka Broker**
Required for publishing `feedback-submitted` events.
#### 3. **Feedback Analytics Consumer**
Reads the Kafka events that THIS API publishes.
#### 4. **Frontend UI**
Uses this API to post and fetch feedback.

All of these are automatically started when you use the **root docker-compose**.

## API .env Credentials

```
POSTGRES_HOST=localhost
POSTGRES_PORT=5433
POSTGRES_USER=postgres
POSTGRES_PASSWORD=FinalProject123
POSTGRES_DB=final_project
POSTGRES_URL=jdbc:postgresql://localhost:5433/final_project
```

## Summary

You don’t run this service alone.  
You run it with **everything else**, using Docker Compose:
```
docker compose up --build
```
This is the correct way to run the entire Provider Feedback Portal.

## 👥 Team

**The Avengers**
- Michael Files
- Gregg Trunnell