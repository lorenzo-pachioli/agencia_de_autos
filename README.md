# Agencia de Autos 🚗

Proyecto fullstack para una agencia de vehículos. Backend con **Spring Boot** y frontend con **Angular**.

---

## ⚙️ Requisitos previos

| Herramienta | Versión mínima |
| ----------- | -------------- |
| Java (JDK)  | 21+            |
| Maven       | 3.9+           |
| Node.js     | 18+            |
| Angular CLI | 17+            |

> No se requiere PostgreSQL ni Docker para el modo local.

---

## 🚀 Cómo levantar el proyecto

### Modo local (H2 en memoria — sin instalaciones extra)

Levanta el backend con una base de datos temporal que se puebla automáticamente con datos de prueba. Se destruye al cerrar.

```bat
./run_local.bat
```

- Frontend: http://localhost:4200
- Backend / Swagger: http://localhost:8080/swagger-ui.html

---

### Modo Supabase (base de datos persistente en la nube)

Requiere tener configurado el archivo `backend/.env` con las credenciales de Supabase.

```bat
./run_supabase.bat
```

---

## 👤 Usuarios de prueba

Todos los usuarios tienen la misma contraseña: **`Password123!`**

| Email                         | Rol           |
| ----------------------------- | ------------- |
| `admin_test@gmail.com`        | ADMINISTRADOR |
| `carlos.garcia@agencia.com`   | VENDEDOR      |
| `lucia.fernandez@agencia.com` | VENDEDOR      |
| `juan.perez@gmail.com`        | USUARIO       |
| `maria.gomez@gmail.com`       | USUARIO       |
| `roberto.rodriguez@gmail.com` | USUARIO       |

---

## 🗄️ Datos de prueba (modo local)

El seeder (`backend/src/main/resources/import.sql`) carga automáticamente:

- **8 marcas**: Toyota, Ford, Volkswagen, Chevrolet, Honda, Renault, Peugeot, Fiat
- **10 modelos** y **10 vehículos** con imágenes
- **4 transacciones** con distintos estados (RESERVA, SENIADO, CANCELADO)
- **4 registros de auditoría** de transacciones
- **3 favoritos** de usuarios

---

## 📁 Estructura del proyecto

```
agencia_de_autos/
├── backend/          # Spring Boot (Java)
├── frontend/         # Angular
├── run_local.bat     # Inicia con H2 en memoria
├── run_supabase.bat  # Inicia contra Supabase
└── README.md
```
