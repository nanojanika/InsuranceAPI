# lightweight JDK base image to use
FROM eclipse-temurin:21-jdk-alpine

# working directory
WORKDIR /app

# copy build files
COPY build/libs/insuranceapi-*.jar app.jar

# expose port
EXPOSE 8080

# run Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
