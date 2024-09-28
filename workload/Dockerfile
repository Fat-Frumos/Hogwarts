FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

COPY workload/pom.xml workload/
COPY workload/src src/

RUN mvn clean package -DskipTests

FROM openjdk:17-slim
COPY --from=build /app/workload/target/trainerWorkload-1.0-SNAPSHOT.jar workload.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "workload.jar"]
