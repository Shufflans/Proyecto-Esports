@echo off
setlocal

echo =========================================
echo COMPILANDO TODOS LOS MICROSERVICIOS
echo =========================================

for %%D in (
    ms-auth
    ms-equipos
    ms-estadisticas
    ms-eureka
    ms-gateway
    ms-juegos
    ms-jugadores
    ms-notificaciones
    ms-partidas
    ms-patrocinadores
    ms-rankings
    ms-torneos
    ms-transferencias
) do (
    echo.
    echo =========================================
    echo Compilando %%D
    echo =========================================

    cd %%D
    call mvnw.cmd clean package -DskipTests

    if errorlevel 1 (
        echo.
        echo ERROR AL COMPILAR %%D
        pause
        exit /b 1
    )

    cd ..
)

echo.
echo =========================================
echo TODOS LOS MICROSERVICIOS COMPILADOS
echo =========================================

pause