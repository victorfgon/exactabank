FROM maven:3.8.4-openjdk-17 AS build
COPY . /home/app
WORKDIR /home/app
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]