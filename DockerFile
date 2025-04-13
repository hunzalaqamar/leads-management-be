FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN ./mvnw clean install -DskipTests || mvn clean install -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-Dserver.port=8080", "-jar", "app.jar"]
