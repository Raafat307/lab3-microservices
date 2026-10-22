# ===== المرحلة 1: البناء =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# نسخ pom.xml الأب والوحدة
COPY pom.xml /app/pom.xml
COPY loyalty/pom.xml /app/loyalty/pom.xml

#  التبعيات
RUN mvn -f /app/loyalty/pom.xml dependency:go-offline -B

# نسخ الكود وابنِ
COPY loyalty/src /app/loyalty/src
RUN mvn -f /app/loyalty/pom.xml clean package -DskipTests

# ===== المرحلة 2: التشغيل =====
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/loyalty/target/loyalty-1.0.0.jar app.jar

EXPOSE 8050

ENTRYPOINT ["java", "-jar", "app.jar"]