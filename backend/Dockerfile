FROM eclipse-temurin:25-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY target/bookstore.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]