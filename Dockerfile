# 1. Используем JDK 17 (или твою версию)
FROM eclipse-temurin:17-jdk-alpine

# 2. Создаём рабочую папку
WORKDIR /app

# 3. Копируем файл сборки JAR
COPY target/hiking-auth-service-0.0.1-SNAPSHOT.jar app.jar

# 4. Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]