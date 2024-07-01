# Use the official Maven image to create a build artifact.
FROM maven:3.8.1-jdk-11 AS build

# Set the working directory inside the container.
WORKDIR /app

# Copy the pom.xml and download the project dependencies.
COPY pom.xml .
RUN mvn dependency:resolve

# Copy the entire project source into the container.
COPY src ./src

# Build the application.
RUN mvn package -DskipTests

# Use the official OpenJDK image to run the application.
FROM openjdk:11-jre-slim

# Set the working directory inside the container.
WORKDIR /app

# Copy the built application from the previous stage.
COPY --from=build /app/target/pearl-ch-services-spring-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on.
EXPOSE 8080

# Define the command to run the application.
ENTRYPOINT ["java", "-jar", "app.jar"]
