# ---------------------- BUILD STAGE ----------------------
FROM eclipse-temurin:24-jdk AS build

# Install Maven manually
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml .

RUN mvn -q dependency:go-offline

COPY src ./src

# builds the JAR
RUN mvn -q clean package -DskipTests


# ---------------------- RUNTIME STAGE ----------------------
FROM eclipse-temurin:24-jre AS runtime

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
