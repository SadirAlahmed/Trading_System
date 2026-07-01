
FROM maven:3.9.9-amazoncorretto-24 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM amazoncorretto:24-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar", "TradingSystem.Main"]
