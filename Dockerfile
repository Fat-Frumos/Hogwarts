FROM maven:3.8.4-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
COPY --from=build /target/gym-0.0.1.jar gym.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gym.jar"]
