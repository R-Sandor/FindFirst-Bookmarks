FROM openjdk:17
COPY server/build/libs/bookmarkit-1.0.0.jar bookmarkit.jar
ENTRYPOINT ["java", "-jar", "bookmarkit.jar"]
