FROM maven:3.9.6-eclipse-temurin-21-alpine as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
run mvn clean package -DskipTests

from eclipse-temurin:21-jre-alpine
copy --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]