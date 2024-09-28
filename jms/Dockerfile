FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y wget && \
    wget https://archive.apache.org/dist/activemq/5.15.9/apache-activemq-5.15.9-bin.tar.gz && \
    tar -xzf apache-activemq-5.15.9-bin.tar.gz && \
    mv apache-activemq-5.15.9 /opt/activemq && \
    rm apache-activemq-5.15.9-bin.tar.gz

EXPOSE 61616 8161

COPY jms/start.sh /app/start.sh
RUN chmod +x /app/start.sh

ENTRYPOINT ["/app/start.sh"]
