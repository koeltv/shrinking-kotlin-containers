FROM gradle:8.3-jdk17-alpine
# Move to working directory
WORKDIR /home/gradle/project
# Copy gradle files
COPY build.special  ./build.gradle.kts
COPY settings.gradle.kts gradle.properties ./
# Download dependencies
RUN ["gradle", "--refresh-dependencies", "--no-daemon"]
# Copy the source code
COPY src ./src
# Build native executable
RUN ["gradle", "shadowJar", "--no-daemon"]
# Run it
ENTRYPOINT ["java", "-jar", "/home/gradle/project/build/libs/service-all.jar"]