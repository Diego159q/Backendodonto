# syntax=docker/dockerfile:1.7
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -Dmaven.test.skip=true package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/dentalcare-backend-2.0.0.jar app.jar
EXPOSE 8080
ENV DB_URL=jdbc:postgresql://localhost:5432/dentalcare \
    DB_USERNAME=postgres \
    DB_PASSWORD=postgres \
    JWT_SECRET=DentalCare2024SecretKeyForJWTTokenGenerationWith256Bits
ENTRYPOINT ["java", "-XX:TieredStopAtLevel=1", "-noverify", "-Xmx256m", "-jar", "app.jar"]
