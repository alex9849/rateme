FROM openjdk:8-jdk-alpine
COPY ./target/rateme.jar app.jar
EXPOSE 9080/tcp
EXPOSE 9443/tcp
ENV TZ=Europe/Berlin
ENTRYPOINT ["java", "-jar", "app.jar"]