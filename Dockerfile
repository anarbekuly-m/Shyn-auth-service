# Этап 1: Собираем проект (используем образ с Maven и Java 17)
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
# Сначала копируем только pom.xml для кэширования зависимостей
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Копируем исходный код и собираем .jar файл
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2: Запускаем проект (чистый образ только с Java 17)
FROM eclipse-temurin:17-jdk
WORKDIR /app
# Копируем готовый .jar из первого этапа
COPY --from=builder /app/target/*.jar app.jar
# Открываем порт 8080
EXPOSE 8080
# Запускаем сервер
ENTRYPOINT ["java", "-jar", "app.jar"]