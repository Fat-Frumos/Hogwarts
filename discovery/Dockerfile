FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY discovery/pom.xml /app/discovery/pom.xml
RUN mvn -f /app/discovery/pom.xml dependency:go-offline

COPY discovery /app/discovery
RUN mvn -f /app/discovery/pom.xml clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /app/discovery/target/discovery-0.0.1-SNAPSHOT.jar discovery.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "config-server.jar"]
