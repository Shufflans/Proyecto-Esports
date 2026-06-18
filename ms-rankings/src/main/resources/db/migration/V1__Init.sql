CREATE TABLE rankings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_jugador BIGINT NOT NULL,
    nombre_jugador VARCHAR(255) NOT NULL,
    id_equipo BIGINT NOT NULL,
    nombre_equipo VARCHAR(255) NOT NULL,
    puntos INT NOT NULL,
    fecha_actualizacion DATETIME NOT NULL
);