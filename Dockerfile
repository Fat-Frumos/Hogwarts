FROM maven:3.8.6-openjdk-17-slim AS BUILDER
ARG VERSION=0.0.1-SNAPSHOT
WORKDIR /build/
COPY pom.xml /build/
COPY src /build/src/
RUN mvn clean package

FROM openjdk:17.0.2-jre-slim
WORKDIR /app/
COPY --from=BUILDER /build/target/gym-0.0.1.jar /app/application.jar
CMD ["java", "-jar", "/app/application.jar"]
