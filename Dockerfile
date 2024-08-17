FROM maven:3.8.4-openjdk-11-slim AS build
ARG VERSION=0.0.1-SNAPSHOT
WORKDIR /build/
COPY pom.xml /build/
COPY src /build/src/
RUN mvn clean package


FROM openjdk:17-jdk-slim
WORKDIR /app/
COPY --from=builder /build/target/gym-${VERSION}.jar /app/application.jar
CMD ["java", "-jar", "/app/application.jar"]
