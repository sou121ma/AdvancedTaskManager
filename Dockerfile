# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight final image
FROM bellsoft/liberica-openjre-alpine:21
WORKDIR /app
COPY --from=build /app/target/*.jar taskapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "taskapp.jar"]