FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY gym-service/pom.xml gym-service/pom.xml
COPY gym-service/src gym-service/src
RUN mvn -f gym-service/pom.xml clean package -DskipTests

COPY jms/pom.xml jms/pom.xml
COPY jms/src jms/src
RUN mvn -f jms/pom.xml clean package -DskipTests

FROM openjdk:21-slim

COPY --from=build /app/jms/target/jms-3.3.3.jar /app/jms.jar
COPY --from=build /app/gym-service/target/gym-service-0.0.1-SNAPSHOT.jar /app/gym-service.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gym-service.jar", "--spring.profiles.active=prod", "--jms.jar=/app/jms.jar"]
