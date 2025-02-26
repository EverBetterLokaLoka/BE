# Use Gradle with JDK 23
FROM gradle:jdk23 AS build

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files first
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Cấp quyền thực thi cho gradlew
RUN chmod +x gradlew

# Download dependencies first (for better caching)
RUN ./gradlew dependencies --no-daemon

# Copy toàn bộ source code
COPY . .

# Build ứng dụng
RUN ./gradlew clean build --no-daemon
