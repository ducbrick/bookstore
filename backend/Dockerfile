# -------------------------
# Stage 1: Build with Maven
# -------------------------
FROM maven:3.9.11-eclipse-temurin-25-noble AS build

# Set the working directory
WORKDIR /app

COPY . .

# Package the application
RUN mvn clean package -DskipTests

# -------------------------
# Stage 2: Run with JRE
# -------------------------
FROM eclipse-temurin:25.0.1_8-jre-noble

# Set work directory for running the app
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
