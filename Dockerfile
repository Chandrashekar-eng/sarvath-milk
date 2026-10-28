# Stage 1: Build the Spring Boot application using Maven & Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY  Sarvathapp/pom.xml .
Copy Sarvathapp/src ./src
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2: Create a lightweight runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/milk-brand-1.0.0.jar /app/milk-brand-1.0.0.jar

# Render passes dynamic port via PORT environment variable
ENV PORT=8080
EXPOSE 8080

# Keep memory usage low for Render's free tier (512MB RAM limit)
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "milk-brand-1.0.0.jar"]