FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongo:27017/rooms", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


#FROM maven:3.6.3-openjdk-11-slim AS builder
#WORKDIR /opt/app
#COPY ./pom.xml /opt/app
#RUN mvn dependency:go-offline
#COPY . /opt/app
#RUN mvn package -DskipTests && java -Djarmode=layertools -jar target/app-0.0.1.jar extract

#FROM openjdk:11-slim
#WORKDIR /webapp
#COPY --from=builder /opt/app/dependencies/ ./
#COPY --from=builder /opt/app/spring-boot-loader ./
#COPY --from=builder /opt/app/snapshot-dependencies/ ./
#COPY --from=builder /opt/app/application/ ./
#CMD ["java", "org.springframework.boot.loader.JarLauncher"]