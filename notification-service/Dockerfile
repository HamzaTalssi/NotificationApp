FROM adoptopenjdk:11-jre-hotspot

COPY target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service-0.0.1-SNAPSHOT.jar

WORKDIR /app

EXPOSE 8085

CMD ["java", "-jar", "notification-service-0.0.1-SNAPSHOT.jar","--spring.config.location=classpath:/application.yml,file:/configuration/config.yml"]
