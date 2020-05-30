FROM openjdk:8-jdk-alpine
COPY target/rateme.jar app.jar
EXPOSE 9080/tcp
ENV TZ=Europe/Berlin
ENV IS_DOCKER=true
ENTRYPOINT ["java", "-jar", "app.jar"]