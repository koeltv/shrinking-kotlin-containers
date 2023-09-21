FROM bellsoft/liberica-runtime-container:jdk-17.0.8.1-slim-musl
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
# Run it
ENTRYPOINT ["java", "-jar", "/home/project/build/libs/service-all.jar"]