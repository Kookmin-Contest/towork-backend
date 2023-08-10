FROM gradle:8.1.1 as builder
WORKDIR /app

COPY build.gradle settings.gradle ./
RUN gradle --build-cache compileJava --parallel

COPY . .
RUN gradle bootJar --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /srv
COPY --from=builder /app/build/libs/*.jar ./app.jar

EXPOSE 8080
USER root
ENTRYPOINT ["java", "-jar", "app.jar"]


