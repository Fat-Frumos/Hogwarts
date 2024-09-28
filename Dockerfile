FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY workload/pom.xml /app/workload/pom.xml
RUN mvn -f /app/workload/pom.xml dependency:go-offline

COPY workload /app/workload
RUN mvn -f /app/workload/pom.xml clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /app/workload/target/workload-0.0.1-SNAPSHOT.jar workload.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "workload.jar"]
