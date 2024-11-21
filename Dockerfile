FROM openjdk:21-jdk

MAINTAINER seojin Yoon <4014133@gmail.com>

LABEL maintainer="Seojin Yoon <4014133@gmail.com>" \
      description="somemore API server Docker Image" \
      version="1.0"

COPY build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
