FROM eclipse-temurin:17-jdk-alpine

ARG db_url
ARG db_username
ARG db_password
ENV DATABASE_URL $db_url
ENV DATABASE_USERNAME $db_username
ENV DATABASE_PASSWORD $db_password

VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080