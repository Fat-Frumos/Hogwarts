FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY jms/pom.xml /app/jms/pom.xml
RUN mvn -f /app/jms/pom.xml dependency:go-offline

COPY jms /app/jms
RUN mvn -f /app/jms/pom.xml clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /app/gym-service/target/jms-0.0.1-jar-with-dependencies.jar jms.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "jms.jar"]


COPY gym-service/pom.xml /app/gym-service/pom.xml
RUN mvn -f /app/gym-service/pom.xml dependency:go-offline

COPY gym-service /app/gym-service
RUN mvn -f /app/gym-service/pom.xml clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /app/gym-service/target/gym-service-0.0.1-SNAPSHOT.jar gym-service.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gym-service.jar"]
