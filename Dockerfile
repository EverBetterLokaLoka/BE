# Use official Gradle image as build stage
FROM gradle:latest AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the application
RUN gradle clean build --no-daemon

# Use Eclipse Temurin JDK 23 as runtime image
FROM eclipse-temurin:23-jdk

# Set working directory
WORKDIR /app

# Copy JAR file from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose application port (adjust if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
