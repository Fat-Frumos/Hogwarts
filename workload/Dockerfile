FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY jms/pom.xml ./jms/pom.xml
COPY workload/pom.xml ./workload/pom.xml

COPY jms/src ./jms/src
COPY workload/src ./workload/src

RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /app/workload/target/trainerWorkload-1.0-SNAPSHOT.jar workload.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "workload.jar"]
