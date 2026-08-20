# Agencia de Autos — API REST

Sistema backend para gestión de inventario y operaciones comerciales de vehículos.

## Deploy 

1. La rama deployada en Render es /feature/migracion-a-postgresql: 

   https://agencia-de-autos.onrender.com/swagger-ui/index.html#

2. Los usuarios disponibles para probarla son:

    - ADMINISTRADOR: admin_test@gmail.com , Password123! (se crea solo por base de datos)
    - VENDEDOR: carlos.garcia@agencia.com , Password123! (Los crea solo el Administrador)
    - CLIENTE: Se puede registrar libremente

3. O alternativamente usar el frontend en netlify:

   - https://agencia-de-autos.netlify.app/

## Tecnologías

- Java 21 · Spring Boot 4.0.6
- Spring Security + JWT ⚠️ _(pendiente agregar al pom.xml)_
- Spring Data JPA (en master es MySQL y en /feature/migracion-a-postgresql es PostgreSQL)
- Lombok · MapStruct 1.6.3
- Swagger/OpenAPI ⚠️ _(pendiente agregar al pom.xml)_

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

| Rol              | Descripción                             |
| ---------------- | --------------------------------------- |
| `CLIENTE`        | Consulta vehículos y gestiona favoritos |
| `VENDEDOR`       | Gestiona vehículos y transacciones      |
| `ADMINISTRACION` | Acceso completo, gestión de usuarios    |

## Convenciones

- **Idioma:** español para entidades, atributos y métodos de dominio
- **Clases/Enums:** `UpperCamelCase` → `VehiculoService`
- **Atributos/Variables:** `camelCase` → `precioFinal`
- **Constantes:** `UPPER_SNAKE_CASE` → `JWT_EXPIRATION_MS`
- **DTOs:** sufijo `RequestDTO` / `ResponseDTO` / `ResumenDTO`
- **Métodos de servicio:** `obtener` / `listar` / `crear` / `actualizar` / `eliminar` / `cambiarEstado`
- **Columnas BD:** `snake_case` explícito vía `@Column(name = "...")`

## Git y Jira

Para que Jira pueda asociar los commits y ramas a las tareas automáticamente, es fundamental incluir la **llave del ticket** (ej: `SCRUM-08`) en los nombres de rama y mensajes de commit.

### Ramas (Branches)

Estructura sugerida: `tipo/LLAVE-numero-descripcion`

- **Nuevas funcionalidades:** `feature/SCRUM-08-gestion-catalogo`
- **Correcciones:** `fix/SCRUM-23-error-mapeo-usuario`
- **Refactorización:** `refactor/SCRUM-05-limpieza-entidades`

### Commits

Estructura: `LLAVE-numero: descripción breve en minúsculas`

- **Ejemplo:** `AG-10: crear entidad vehiculo y su repositorio`
- **Ejemplo:** `AG-25: corregir validación de email duplicado`

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

| Nombre | Módulo                                               |
|  | ---------------------------------------------------- |
| Emiliano Osorio | Catálogo (Marca, Modelo, Vehículo, Imágenes)         |
| Lorenzo Pachioli | Operaciones (Transacción, Método de Pago, Historial) |
| Agustin Avalos | Seguridad, Usuarios, Favoritos, Infraestructura      |
