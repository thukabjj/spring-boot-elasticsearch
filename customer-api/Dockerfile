FROM maven:3.8.1-jdk-11 as builder
COPY src ./src
COPY pom.xml .
RUN mvn clean package -DskipTests
RUN java -Djarmode=layertools -jar target/customer-api-0.0.1-SNAPSHOT.jar extract
# ARG JAR_FILE=target/*.jar
# COPY ${JAR_FILE} application.jar


FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder application/ ./
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]