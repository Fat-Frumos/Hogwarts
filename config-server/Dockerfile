FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

COPY . .

WORKDIR /app/config-server

RUN mvn clean package -DskipTests

FROM openjdk:17-slim
COPY --from=build /app/config-server/target/config-server-0.0.1-SNAPSHOT.jar config-server.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "config-server.jar"]
