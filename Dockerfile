# Use official OpenJDK 17 image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom files
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Copy source code
COPY src ./src

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Copy the jar to the final image
COPY target/*.jar app.jar

# Expose port
EXPOSE 9090

# Run the jar
ENTRYPOINT ["java","-jar","app.jar"]
