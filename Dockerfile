FROM gradle:8.11.1-jdk21 AS build

WORKDIR /app

COPY gradle gradle
COPY config config
COPY gradlew build.gradle settings.gradle ./

RUN ./gradlew --no-daemon build -x test -x check

COPY src src

RUN ./gradlew --no-daemon build -x test -x check

FROM openjdk:21-jdk-slim AS runtime

RUN apt-get update && apt-get install -y bash curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /app/build/libs/data-pipeline.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]