FROM openjdk:17-jdk-alpine
COPY build/libs/authorization_service-0.0.2-SNAPSHOT.jar /authorization_service-0.0.2-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "/authorization_service-0.0.2-SNAPSHOT.jar"]