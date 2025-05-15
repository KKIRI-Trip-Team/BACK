FROM gradle:8.7-jdk17 AS builder
WORKDIR /app
COPY . .
ENV GRADLE_USER_HOME=/app/.gradle
RUN gradle build -x test --no-daemon --refresh-dependencies

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
