CREATE TABLE equipo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_equipo VARCHAR(50) NOT NULL UNIQUE,
    region VARCHAR(255) NOT NULL,
    fecha_fundacion DATE NOT NULL,
    ranking_mundial INT(4),
    activo BOOLEAN NOT NULL
);

CREATE TABLE staff_tecnico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_staff VARCHAR(255) NOT NULL,
    rol VARCHAR(55) NOT NULL,
    salario_mensual DOUBLE(10,2),
    activo BOOLEAN NOT NULL,
    equipo_id BIGINT NOT NULL,
    FOREIGN KEY (equipo_id) REFERENCES equipo(id)
);

CREATE TABLE roster_historico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_jugador BIGINT NOT NULL,
    nickname VARCHAR(55) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    equipo_id BIGINT NOT NULL,

    FOREIGN KEY (equipo_id) REFERENCES equipo(id)
);