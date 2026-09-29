@echo off
setlocal

cd /d "%~dp0"

echo ==============================================================
echo  Agencia de Autos - Modo Local Temporal (H2 + Seeder)
echo ==============================================================

set DB_URL=jdbc:h2:mem:agenciadb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
set DB_USERNAME=sa
set DB_PASSWORD=
set DB_DRIVER=org.h2.Driver
set JPA_DIALECT=org.hibernate.dialect.H2Dialect

echo [1/2] Iniciando Backend Spring Boot (H2 en memoria)...
start "Agencia Autos - Backend Local" cmd /k "cd /d "%~dp0backend" && mvn spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=%DB_URL% --spring.datasource.username=%DB_USERNAME% --spring.datasource.password=%DB_PASSWORD% --spring.datasource.driver-class-name=%DB_DRIVER% --spring.jpa.database-platform=%JPA_DIALECT% --spring.sql.init.mode=always --spring.jpa.defer-datasource-initialization=true""

echo [2/2] Iniciando Frontend Angular (Puerto 4200)...
start "Agencia Autos - Frontend" cmd /k "cd /d "%~dp0frontend" && npm start"

echo.
echo Esperando 7 segundos para la inicializacion de servicios...
ping -n 8 127.0.0.1 >nul

start chrome "http://localhost:4200" 2>nul || start "http://localhost:4200"
start chrome "http://localhost:8080/swagger-ui.html" 2>nul || start "http://localhost:8080/swagger-ui.html"

echo.
echo ==============================================================
echo  Modo Local listo!
echo    Frontend (Angular): http://localhost:4200
echo    Backend (Swagger UI): http://localhost:8080/swagger-ui.html
echo    DB: H2 Postgres Mode (Memoria temporal - se destruye al cerrar)
echo ==============================================================
pause
