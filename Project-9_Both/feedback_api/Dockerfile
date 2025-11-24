# ============================
# 🧱 1. BUILD STAGE (Maven + JDK)
# ============================
FROM eclipse-temurin:24-jdk AS build

# Install Maven manually
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy pom.xml first for caching
COPY pom.xml .

# Pre-download dependencies (cache)
RUN mvn -q dependency:go-offline

COPY src ./src

# builds the JAR
RUN mvn -q clean package -DskipTests

# ============================
# 🚀 2. RUNTIME STAGE (JRE ONLY)
# ============================
FROM eclipse-temurin:24-jre AS runtime

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose API port
EXPOSE 8080

# Run Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
