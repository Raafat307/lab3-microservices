FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml /app/pom.xml
COPY payment/pom.xml /app/payment/pom.xml

RUN mvn -f /app/payment/pom.xml dependency:go-offline -B

COPY payment/src /app/payment/src
RUN mvn -f /app/payment/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/payment/target/payment-1.0.0.jar app.jar

EXPOSE 8060

ENTRYPOINT ["java", "-jar", "app.jar"]