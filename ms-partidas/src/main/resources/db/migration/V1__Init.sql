CREATE TABLE partidas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_torneo BIGINT NOT NULL,
    nombre_torneo VARCHAR(255) DEFAULT NULL,
    id_equipo_local BIGINT NOT NULL,
    nombre_equipo_local VARCHAR(255) DEFAULT NULL,
    id_equipo_visitante BIGINT NOT NULL,
    nombre_equipo_visitante VARCHAR(255) DEFAULT NULL,
    fecha_hora DATETIME(6) NOT NULL,
    duracion_minutos INT DEFAULT NULL,
    marcador_local INT DEFAULT NULL,
    marcador_visitante INT DEFAULT NULL,
    id_equipo_ganador BIGINT DEFAULT NULL,
    estado VARCHAR(50) NOT NULL
)