FROM openjdk:8-jdk-alpine
COPY ./KafkaReader/target/KafkaReader.jar /app.jar

ENTRYPOINT java -Dkafka.host="${KAFKA_HOST}" -Dkafka.port="${KAFKA_PORT}" -Dkafka.topic="${KAFKA_TOPIC}" -jar /app.jar