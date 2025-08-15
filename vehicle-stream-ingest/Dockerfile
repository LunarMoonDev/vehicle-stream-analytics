# build stage
FROM gradle:8.14.3-jdk17-ubi-minimal AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# run stage
FROM openjdk:17-ea-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]