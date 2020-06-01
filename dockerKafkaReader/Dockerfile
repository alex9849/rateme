FROM openjdk:8-jdk-alpine
COPY ./KafkaReader/target/KafkaReader.jar /app.jar
COPY ./dockerKafkaReader/consumer.properties /
ENTRYPOINT ["java","-jar","/app.jar", "/consumer.properties"]