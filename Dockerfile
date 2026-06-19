# Paso 1: Entorno de compilación con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar el archivo de configuración de Maven y el código fuente
COPY pom.xml .
COPY src ./src

# Compilar y empaquetar el proyecto saltando los tests para acelerar el despliegue
RUN mvn clean package -DskipTests

# Paso 2: Entorno de ejecución ligero con Java 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el JAR generado desde la etapa de compilación
# Usamos el nombre exacto definido en tu pom.xml
COPY --from=build /app/target/agencia_de_autos-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto estándar que usa Render
EXPOSE 8080

# Comando para ejecutar la API
ENTRYPOINT ["java", "-jar", "app.jar"]
