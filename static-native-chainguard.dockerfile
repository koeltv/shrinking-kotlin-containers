## Stage 1: Build the Application
FROM ghcr.io/graalvm/native-image-community:17-muslib AS builder
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
RUN ["./gradlew", "nativeCompile", "--no-daemon", "-Pstatic"]

## Stage 2: Create a Minimal Container
FROM cgr.dev/chainguard/static
USER root
EXPOSE 8000
# Copy the executable from the builder container
COPY --from=builder /home/project/build/native/nativeCompile/service /service
# Run the executable
ENTRYPOINT ["/service"]