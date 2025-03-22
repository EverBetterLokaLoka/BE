# Build stage
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app

# Increase memory available to Gradle
ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx2g -Dorg.gradle.daemon=false"

# Copy gradle configuration files first for better layer caching
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./

# Give execution permission to gradlew
RUN chmod +x ./gradlew

# Run a gradle task to download dependencies
RUN ./gradlew dependencies --no-daemon || return 0

# Copy source code
COPY src/ src/

# Build the application
RUN ./gradlew clean build --refresh-dependencies --no-daemon -x test

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]