# Proyecto Esports - Microservicios Backend Con SpringBoot

## Descripción del proyecto
Este proyecto corresponde al desarrollo de microservicios para la gestión de un ecosistema de deportes electrónicos (**E-sports**).
Este sistema permite poder administrar equipos, jugadores, torneos, partidas, rankings, estadísticas, patrocinadores, transferencias, notificaciones y además
tiene una integración de seguridad con la autenticación de los usuarios. Todo esto a través de servicios independientes que se comunican mediante REST y se centralizan en un API GATEWAY

##  Integrantes del Equipo

- Camilo Covarrubias
- Ignacio Vallejos

## Microservicios Implementados:

- **MS-AUTH**: Mantiene la autenticacion y la gestión de los usuarios.
- **MS-EQUIPOS**: Gestiona los equipos de E-sports.
- **MS-ESTADÍSTICAS**: Estadística de todos los jugadores dentro de las partidas.
- **MS-EUREKA**: Servidor de descubrimiento de servicios.
- **MS-GATEWAY**: API GATEWAT - Todos los microservicios convergen aquí.
- **MS-JUEGOS**: Catálogo completo y gestión de los juegos/títulos.
- **MS-JUGADORES**: Gestión de los jugadores.
- **MS-NOTIFICACIONES**: Envio y gestión de notificaciones.
- **MS-PARTIDAS**: Gestión de las partidas que se organicen.
- **MS-PATROCINADORES**: Gestión de los patrocinadores por equipos.
- **MS-RANKINGS**: Ranking de todos los equipos y jugadores.
- **MS-TORNEOS**: Gestión de torneos.
- **MS-TRANSFERENCIAS**: Gestión de transferencia de los jugadores entre equipos.

## Comunicación y Arquitectura

Todos los microservicios se registran en Eureka y se enrutan directamente en el API GATEWAY, el cuál está expuesto en el puerto 8080, gracias a esto, mantenemos centralizado todos los accesos mediante los prefijos de los servicios.
Todos los microservicios de la aplicación siguen la misma convención:
- "/api/v1/{nombre-servicio}"

## Documentación Swagger con OpenAPI

Cada uno de los microservicios expone su documentación interactiva utilizando la ruta:
- http://localhost:{puerto-del-microservicio}/swagger-ui.html

## Pruebas Unitarias

El proyecto cuenta con pruebas unitarias con JUnit y Mokcito sobre las capas de Service, Controller y Repository de los microservicios.
