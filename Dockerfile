FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine

ARG DEPENDENCY=/workspace/app/target/dependency
ARG db_url
ARG db_username
ARG db_password
ENV DATABASE_URL $db_url
ENV DATABASE_USERNAME $db_username
ENV DATABASE_PASSWORD $db_password

VOLUME /tmp
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
#COPY target/*.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.drewm.FlashcardsApiApplication"]
EXPOSE 8080