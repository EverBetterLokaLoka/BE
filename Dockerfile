# Use Gradle with JDK 23
FROM gradle:jdk23 AS build

# Set working directory
WORKDIR /app

# Copy only Gradle wrapper and build files first
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x gradlew

# Download dependencies first (for better caching)
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the project files
COPY . .

# Build the application
RUN ./gradlew clean build --no-daemon
