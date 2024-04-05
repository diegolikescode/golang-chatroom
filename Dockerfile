FROM maven:3.8.3-openjdk-17
# FROM openjdk:17-alpine

WORKDIR /app

# COPY pom.xml /app/pom.xml
# COPY src /app/src

# RUN mvn -f /app/pom.xml clean package

COPY ./target/rinhajava-0.0.69.jar /app/rinhajava.jar

EXPOSE 6969

CMD ["java", "-jar", "rinhajava.jar"]

