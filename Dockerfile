FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY $JAR_FILE /srv/server.jar

ENTRYPOINT ["java", "-jar", "/srv/server.jar"]
