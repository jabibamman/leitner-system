# Render n'a pas de runtime Java natif : le service se deploie via cette image.

# --- Etape de build ---------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Les dependances sont resolues avant de copier les sources : tant que le pom
# ne change pas, cette couche est reprise du cache.
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# --- Image finale -----------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# L'application ne tourne pas en root.
RUN addgroup -S leitner && adduser -S leitner -G leitner
USER leitner

COPY --from=build /build/target/*.jar app.jar

# Render fournit le port a ecouter via PORT ; 8080 sert de valeur locale.
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
