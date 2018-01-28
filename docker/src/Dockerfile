FROM openjdk:8-jdk-alpine
COPY ./microservice-discussions-1.0.0.jar /usr/src/microservice-discussions-1.0.0.jar
WORKDIR /usr/src/

EXPOSE 8080

ENTRYPOINT ["java", "-jar","microservice-discussions-1.0.0.jar"]