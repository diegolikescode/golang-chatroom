FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

# lib/postgresql-42.7.3.jar

COPY pom.xml pom.xml
COPY mvnw mvnw
COPY .mvn .mvn

COPY src ./src

RUN mvn dependency:resolve
RUN mvn clean install

# EXPOSE 6969

# COPY /app/target/rinha-java.jar /app/rinha-java.jar

CMD ["java", "-jar", "/app/target/rinha-java.jar"]

