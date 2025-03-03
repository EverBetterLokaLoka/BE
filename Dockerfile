# Use Gradle with JDK 23
FROM eclipse-temurin:23-jdk as build

# Set working directory
WORKDIR /app

# Copy toàn bộ source code vào container
COPY . .

# Build ứng dụng với Gradle
RUN ./gradlew clean build -x test

# Stage chạy ứng dụng
FROM eclipse-temurin:17-jre

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy file JAR từ giai đoạn build
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port ứng dụng (thay thế 8080 bằng port của bạn nếu khác)
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]