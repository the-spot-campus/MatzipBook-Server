FROM openjdk:17-ea-11-jdk-slim
VOLUME /logs
COPY build/libs/matzipbook-server-0.0.1-SNAPSHOT.jar Matzipbook.jar
ENTRYPOINT ["java", "-jar", "Matzipbook.jar"]