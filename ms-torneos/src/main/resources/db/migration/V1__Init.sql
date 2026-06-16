CREATE TABLE torneos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_torneo VARCHAR(255) NOT NULL,
    id_juego BIGINT NOT NULL,
    nombre_juego VARCHAR(255) NOT NULL,
    organizador VARCHAR(255) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    total_premio DOUBLE NOT NULL,
    max_equipos INT NOT NULL,
    estado VARCHAR(50) NOT NULL
);