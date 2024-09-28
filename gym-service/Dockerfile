# Use Maven for the build
FROM maven:3.9.8-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy gym-service's pom.xml and source code, then build
COPY gym-service/pom.xml gym-service/pom.xml
COPY gym-service/src gym-service/src
RUN mvn -f gym-service/pom.xml clean package -DskipTests

# Copy jms's pom.xml and source code, then build
COPY jms/pom.xml jms/pom.xml
COPY jms/src jms/src
RUN mvn -f jms/pom.xml clean package -DskipTests

# Production stage
FROM openjdk:21-slim

# Copy the built jms.jar into the gym-service image
COPY --from=build /app/jms/target/jms-0.0.1-jar-with-dependencies.jar /app/jms.jar
COPY --from=build /app/gym-service/target/gym-service-0.0.1-SNAPSHOT.jar /app/gym-service.jar

# Set the working directory
WORKDIR /app

# Expose the necessary port
EXPOSE 8080

# Entry point for the gym-service
ENTRYPOINT ["java", "-jar", "gym-service.jar", "--spring.profiles.active=prod", "--jms.jar=/app/jms.jar"]
