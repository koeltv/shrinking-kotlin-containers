## Stage 1: Build the Application
FROM eclipse-temurin:17-jdk AS builder
# Move to working directory
WORKDIR /home/project
# Copy gradle files
COPY gradle ./gradle
COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
# Download dependencies
RUN ["./gradlew", "--refresh-dependencies", "--no-daemon"]
# Copy the source code
COPY src ./src
# Build native executable
RUN ["./gradlew", "shadowJar", "--no-daemon"]

## Stage 2: Create a Minimal Container
FROM azul/zulu-openjdk-alpine:17.0.8.1-jre-headless
EXPOSE 8000
# Copy the executable from the builder container
COPY --from=builder /home/project/build/libs/*.jar /service.jar
# Run the executable
ENTRYPOINT ["java", "-jar", "/service.jar"]