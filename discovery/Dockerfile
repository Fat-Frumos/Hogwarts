FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

COPY discovery/pom.xml discovery/
COPY discovery/src src/

RUN mvn clean package -DskipTests

FROM openjdk:17-slim
COPY --from=build /app/discovery/target/discovery-0.0.1-SNAPSHOT.jar discovery.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "discovery.jar"]
