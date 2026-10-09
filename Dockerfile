# Multi-stage Docker build for Railway / Render cloud deployment
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:9.0-jdk17-corretto
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/lathikamart.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
