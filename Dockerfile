# Stage 1: Build the application
# We use a base image that has Java 25 and Maven
FROM maven:3.9-eclipse-temurin-25 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Copy the rest of your source code
COPY src ./src

# Run Maven to build the .jar file (and skip tests)
RUN mvn package -DskipTests

# Stage 2: Create the final, small image
# We use a slim Java 25-only image
FROM eclipse-temurin:25-jre

# Set the working directory
WORKDIR /app

# Copy the .jar file we built in Stage 1
# IMPORTANT: Check your pom.xml for the <version>
# It's probably 0.0.1-SNAPSHOT
COPY --from=build /app/target/kaiburr-task-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]
