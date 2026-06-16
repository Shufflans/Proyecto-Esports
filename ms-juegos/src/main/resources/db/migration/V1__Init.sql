CREATE TABLE juego (
    id BIGINT AUTO_INCREMENT NOT NULL,
    nombre_juego VARCHAR(255) NOT NULL,
    genero_juego VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL,
    CONSTRAINT pk_juego PRIMARY KEY (id),
    CONSTRAINT uc_juego_nombre_juego UNIQUE (nombre_juego)
);