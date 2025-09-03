# Etapa 1: build con Gradle 8.9 y JDK 21
FROM gradle:8.9-jdk21 AS build

WORKDIR /app
COPY . .

# Compilamos únicamente el microservicio app-service
RUN ./gradlew :app-service:bootJar -x validateStructure --no-daemon

# Etapa 2: runtime con Java 21 (más liviana que full JDK)
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
COPY --from=build /app/applications/app-service/build/libs/ms-loans.jar app.jar

# Creamos un usuario no root
RUN adduser --disabled-password --gecos '' appuser && chown -R appuser /app
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
