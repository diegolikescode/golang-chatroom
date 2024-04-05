FROM maven:3.8.3-openjdk-17

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY src /app/src

RUN mvn -f /app/pom.xml clean package

COPY ./target/rinhajava-0.0.69.jar /app/rinhajava.jar

EXPOSE 8000

CMD ["java", "-jar", "rinhajava.jar"]
