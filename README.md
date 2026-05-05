# Agencia de Autos — API REST

Sistema backend para gestión de inventario y operaciones comerciales de vehículos.

## Tecnologías

- Java 21 · Spring Boot 4.0.6
- Spring Security + JWT ⚠️ *(pendiente agregar al pom.xml)*
- Spring Data JPA + MySQL
- Lombok · MapStruct 1.6.3
- Swagger/OpenAPI ⚠️ *(pendiente agregar al pom.xml)*

## Dependencias pendientes en `pom.xml`

Agregar antes de la semana 3:

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

<!-- Swagger / OpenAPI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.8</version>
</dependency>
```

## Requisitos

- Java 21+
- MySQL 8+
- Maven 3.8+

## Configuración

1. Crear la base de datos:
```sql
CREATE DATABASE agencia_db;
```

2.Pendiente agregar dependencia para menejo de .env.

La BD se crea automáticamente con `spring.jpa.hibernate.ddl-auto=update` y se puebla con `data.sql`.

## Documentación

Swagger disponible en: `http://localhost:8080/swagger-ui.html`

## Roles

| Rol | Descripción |
|-----|-------------|
| `CLIENTE` | Consulta vehículos y gestiona favoritos |
| `VENDEDOR` | Gestiona vehículos y transacciones |
| `ADMINISTRACION` | Acceso completo, gestión de usuarios |

## Convenciones

- **Idioma:** español para entidades, atributos y métodos de dominio
- **Clases/Enums:** `UpperCamelCase` → `VehiculoService`
- **Atributos/Variables:** `camelCase` → `precioFinal`
- **Constantes:** `UPPER_SNAKE_CASE` → `JWT_EXPIRATION_MS`
- **DTOs:** sufijo `RequestDTO` / `ResponseDTO` / `ResumenDTO`
- **Métodos de servicio:** `obtener` / `listar` / `crear` / `actualizar` / `eliminar` / `cambiarEstado`
- **Columnas BD:** `snake_case` explícito vía `@Column(name = "...")`

## Estructura

```
model/          Entidades JPA + enums
repository/     Interfaces JpaRepository
dto/            request/ y response/
mapper/         Interfaces MapStruct
service/        Lógica de negocio
controller/     Endpoints REST
exception/      Manejo centralizado de errores
security/       JWT + Spring Security
```

## Forma de trabajo 
Repartición tentativa de trabajo para evitar conflictos de código.

| Nombre | Módulo |
|--------|--------|
| Persona A | Catálogo (Marca, Modelo, Vehículo, Imágenes) |
| Persona B | Operaciones (Transacción, Método de Pago, Historial) |
| Persona C | Seguridad, Usuarios, Favoritos, Infraestructura |