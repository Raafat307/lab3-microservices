FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml /app/pom.xml
COPY reservation/pom.xml /app/reservation/pom.xml

RUN mvn -f /app/reservation/pom.xml dependency:go-offline -B

COPY reservation/src /app/reservation/src
RUN mvn -f /app/reservation/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/reservation/target/reservation-1.0.0.jar app.jar

EXPOSE 8070

ENTRYPOINT ["java", "-jar", "app.jar"]