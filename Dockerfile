FROM openjdk:17-slim AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar
CMD java -jar app.jar