FROM openjdk:11-jre-slim

COPY target/notification-manager-0.0.1-SNAPSHOT.jar /app/notification-manager-0.0.1-SNAPSHOT.jar

WORKDIR /app

EXPOSE 8089

CMD ["java", "-jar", "notification-manager-0.0.1-SNAPSHOT.jar"]
