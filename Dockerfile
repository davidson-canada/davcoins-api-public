#
# Build stage
#
FROM maven:3.8.5-openjdk-17-slim AS build
ARG type_deploy
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn --quiet -P $type_deploy -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:17.0.2-jdk-slim
COPY --from=build /home/app/target/davcoins-api-0.0.1-SNAPSHOT.jar /usr/local/lib/davcoins-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/davcoins-api-0.0.1-SNAPSHOT.jar"]
