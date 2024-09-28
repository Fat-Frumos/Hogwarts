FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY jms/pom.xml ./jms/pom.xml
COPY gym-service/pom.xml ./gym-service/pom.xml

COPY jms/src ./jms/src
COPY gym-service/src ./gym-service/src

RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /app/gym-service/target/gym-service-0.0.1-SNAPSHOT.jar gym-service.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gym-service.jar"]
