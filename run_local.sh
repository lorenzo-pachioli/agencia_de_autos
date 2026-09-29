#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "=============================================================="
echo " Agencia de Autos - Modo Local Temporal (H2 + Seeder)"
echo "=============================================================="

DB_URL="jdbc:h2:mem:agenciadb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL"
DB_USERNAME="sa"
DB_PASSWORD=""
DB_DRIVER="org.h2.Driver"
JPA_DIALECT="org.hibernate.dialect.H2Dialect"

echo "[1/2] Iniciando Backend (H2 en memoria)..."
(cd "$SCRIPT_DIR/backend" && mvn spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=$DB_URL --spring.datasource.username=$DB_USERNAME --spring.datasource.password=$DB_PASSWORD --spring.datasource.driver-class-name=$DB_DRIVER --spring.jpa.database-platform=$JPA_DIALECT --spring.sql.init.mode=always --spring.jpa.defer-datasource-initialization=true") &

echo "[2/2] Iniciando Frontend (Angular en puerto 4200)..."
(cd "$SCRIPT_DIR/frontend" && npm start) &

echo "Esperando inicialización de servicios..."
sleep 7

if command -v cmd.exe >/dev/null 2>&1; then
    cmd.exe /c start chrome "http://localhost:4200" || cmd.exe /c start "http://localhost:4200"
    cmd.exe /c start chrome "http://localhost:8080/swagger-ui.html" || cmd.exe /c start "http://localhost:8080/swagger-ui.html"
elif command -v open >/dev/null 2>&1; then
    open "http://localhost:4200"
    open "http://localhost:8080/swagger-ui.html"
elif command -v xdg-open >/dev/null 2>&1; then
    xdg-open "http://localhost:4200"
    xdg-open "http://localhost:8080/swagger-ui.html"
fi
