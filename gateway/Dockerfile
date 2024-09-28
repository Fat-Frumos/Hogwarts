# Dockerfile for gateway

FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

COPY gateway/pom.xml gateway/
COPY gateway/src src/

RUN mvn clean package -DskipTests

FROM openjdk:17-slim
COPY --from=build /app/gateway/target/gateway-0.0.1-SNAPSHOT.jar gateway.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gateway.jar"]
