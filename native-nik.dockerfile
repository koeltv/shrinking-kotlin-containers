## Stage 1: Build the Application
FROM bellsoft/liberica-native-image-kit-container:jdk-17-nik-22-musl AS builder
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
RUN ["./gradlew", "nativeCompile", "--no-daemon"]

## Stage 2: Create a Minimal Container
FROM bellsoft/alpaquita-linux-base
EXPOSE 8000
# Copy the executable from the builder container
COPY --from=builder /home/project/build/native/nativeCompile/service /service
# Run the executable
ENTRYPOINT ["/service"]