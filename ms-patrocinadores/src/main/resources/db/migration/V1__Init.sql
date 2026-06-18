CREATE TABLE patrocinadores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_marca VARCHAR(255) NOT NULL,
    id_equipo BIGINT NOT NULL,
    monto_anual DOUBLE,
    fecha_inicio DATE,
    fecha_fin DATE
);