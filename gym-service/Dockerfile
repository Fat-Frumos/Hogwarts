FROM maven:3.8.6-openjdk-21 AS build

WORKDIR /app

COPY pom.xml gym-service/pom.xml
COPY ../jms/pom.xml jms/pom.xml
COPY ../jms/src jms/src
COPY src gym-service/src

RUN mvn -f jms/pom.xml clean install -DskipTests
RUN mvn -f gym-service/pom.xml clean package -DskipTests -e -X

RUN ls -l gym-service/target

FROM openjdk:21-jdk-slim
COPY gym-service/target/gym-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
