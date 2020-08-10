FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/service-a.jar
COPY ${JAR_FILE} service-a.jar
ENTRYPOINT ["java","-jar","/service-a.jar"]