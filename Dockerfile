FROM maven:3.9.8-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /target/gym-0.0.1.jar gym.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gym.jar"]
