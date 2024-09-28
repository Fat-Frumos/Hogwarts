FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY jms/pom.xml /app/jms/pom.xml
RUN mvn -f /app/jms/pom.xml dependency:go-offline

COPY jms /app/jms
RUN mvn -f /app/jms/pom.xml clean package -DskipTests

COPY gym-service/pom.xml /app/gym-service/pom.xml
RUN mvn -f /app/gym-service/pom.xml dependency:go-offline

COPY gym-service /app/gym-service
RUN mvn -f /app/gym-service/pom.xml clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build target/gym-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
