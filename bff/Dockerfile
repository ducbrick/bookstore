FROM eclipse-temurin:25-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY target/bookstore-frontend-0.0.1-SNAPSHOT.jar bff.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/bff.jar"]