FROM openjdk:17-oracle
COPY ../target/users-0.0.1-SNAPSHOT.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
