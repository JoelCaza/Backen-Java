# ==========================================
# Etapa 1: Construcción (Build) con Java 21
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiamos el pom.xml para descargar dependencias primero
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos todo el código fuente y compilamos
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# Etapa 2: Ejecución (Run) con Java 21
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos únicamente el .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto de Spring Boot
EXPOSE 8080

# Arrancamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]