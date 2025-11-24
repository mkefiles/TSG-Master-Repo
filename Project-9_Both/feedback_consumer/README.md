# Provider Feedback Portal - Consumer (Spring Boot + Kafka)

**THE PROJECT MAY NEED CERTAIN PATHS UPDATED AS THE NAME AND PATH WAS UPDATED -- CODE MAY NOT RUN UNTIL UPDATED**

A lightweight Spring Boot microservice that listens to the **`feedback-submitted`** Kafka topic, deserializes feedback events, logs structured JSON, and simulates downstream analytics processing for the Provider Feedback Portal.

## ✨ Features

- **Kafka Consumer**  
  Subscribes to the `feedback-submitted` topic using a customized listener container and JSON deserialization.

- **Feedback Event Processing**  
  Logs structured event data and simulates analytics handling (e.g., forwarding to BI systems, dashboards, etc.).

- **Strict JSON Deserialization**  
  Configured `ObjectMapper` with JavaTimeModule and date/time handling for `OffsetDateTime`.

- **Health Check Endpoint**  
  Simple `/health` controller to verify that the service is running.

- **Containerized Deployment**  
  Includes a production-ready multi-stage Dockerfile.

## 🚀 Running the Service (Local Development)

### 1. Prerequisites
Make sure you have the following installed:

- **Java 17+** (or the version specified in your project)
- **Maven**
- **Docker Desktop** (if running with containers)
- A running **Kafka broker** (usually provided via the top-level `docker-compose.yml` in the master repo)
### 2. Running with Maven

From the project root:
```
mvn spring-boot:run
```

### 3. Running with Docker
A multi-stage Dockerfile is included.

To build and run:
```
docker build -t feedback-analytics-consumer .
docker run -p 8085:8080 feedback-analytics-consumer

```
Or, if using the **master repo's docker-compose**, this service will start automatically along with:

- feedback-api (producer)
- frontend-feedback-ui
- Kafka
- Postgres

## 🔄 How It Works (Event Flow)

1. **feedback-api** publishes a `FeedbackEvent` to the `feedback-submitted` Kafka topic.
2. **feedback-analytics-consumer** receives the event using a `@KafkaListener`.
3. The JSON payload is deserialized into a `FeedbackEvent` model.
4. The listener logs the event in a structured, readable format.

## 🩺 API Endpoints

### Health Check
Verify the service is running.

Run this command in your browser:
```
http://localhost:8085/health
```
or check in terminal:
```
curl -i http://localhost:8085/health
```

## 🧪 Testing

Unit tests validate listener behavior:

- Handling of valid events
- Graceful handling of `null` events

Import as Maven Project(if IntelliJ does not auto-detect it):
Right-click the `pom.xml` file and choose **"Add as Maven Project."**

Run all tests:
```
mvn test
```
## 👥 Team

**The Avengers**
- Michael Files
- Gregg Trunnell  