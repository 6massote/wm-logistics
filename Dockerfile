# syntax=docker/dockerfile:1

# ---- build: compile and package the executable jar (the integration tests need Docker, so run them outside)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml ./
COPY wm-web/pom.xml wm-web/
# Resolve dependencies first so this layer is cached until a pom changes
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -pl wm-web dependency:go-offline
COPY wm-web/src wm-web/src
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -pl wm-web -DskipTests package

# ---- runtime: JRE only, non-root
FROM eclipse-temurin:21-jre AS runtime
RUN useradd --system --no-create-home --shell /usr/sbin/nologin wm
USER wm
WORKDIR /app
COPY --from=build /build/wm-web/target/wm-logistics.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
