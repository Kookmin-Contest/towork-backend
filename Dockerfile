FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} /srv/server.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/srv/server.jar"]