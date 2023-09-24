# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

COPY . .

RUN chmod +x ./gradlew
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon clean bootJar
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*-SNAPSHOT.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

COPY ./scripts/docker-spring-entrypoint.sh /scripts/docker-spring-entrypoint.sh
RUN chmod +x /scripts/docker-spring-entrypoint.sh

ENTRYPOINT ["java","-cp","app:app/lib/*","com.towork.api.ApiApplication"]
