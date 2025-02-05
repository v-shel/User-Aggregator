FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/user-aggregator-beta-1.jar user-aggregator.jar
ENTRYPOINT ["java", "-jar", "user-aggregator.jar"]