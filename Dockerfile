# 1. Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# 2. Set the working directory in the container
WORKDIR /app

# 3. Define build arguments
ARG API_KEY
ARG DB_PASSWORD
ARG DB_URL
ARG DB_USERNAME
ARG JWT_SECRET_KEY
ARG KAKAO_API_KEY

# 4. Set environment variables
ENV API_KEY=$API_KEY
ENV DB_PASSWORD=$DB_PASSWORD
ENV DB_URL=$DB_URL
ENV DB_USERNAME=$DB_USERNAME
ENV JWT_SECRET_KEY=$JWT_SECRET_KEY
ENV KAKAO_API_KEY=$KAKAO_API_KEY

# 5. Copy the local JAR file to the container
COPY ./build/libs/oneeat-0.0.1-SNAPSHOT.jar /app/oneeat-0.0.1-SNAPSHOT.jar

# 6. Make port 8080 available to the world outside the container
EXPOSE 8080

# 7. Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "/app/oneeat-0.0.1-SNAPSHOT.jar"]
