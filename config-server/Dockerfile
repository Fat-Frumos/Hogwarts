FROM maven:3.8.4-openjdk-21-slim AS build
WORKDIR /app

COPY config-server/pom.xml /app/config-server/pom.xml
RUN mvn -f /app/config-server/pom.xml dependency:go-offline

COPY config-server /app/config-server
RUN mvn -f /app/config-server/pom.xml clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /app/config-server/target/config-server-0.0.1-SNAPSHOT.jar config-server.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "config-server.jar"]
