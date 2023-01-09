FROM openjdk:20-ea-17-slim
ARG JAR_FILE=impl/target/social-service-impl-1.0.0-SNAPSHOT-exec.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
