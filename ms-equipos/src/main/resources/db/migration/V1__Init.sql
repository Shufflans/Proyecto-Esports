CREATE TABLE equipo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombreEquipo VARCHAR(50) NOT NULL UNIQUE,
    region VARCHAR(255) NOT NULL,
    fechaFundacion DATE NOT NULL,
    rankingMundial INT(4),
    activo BOOLEAN NOT NULL
);

CREATE TABLE staff_tecnico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombreStaff VARCHAR(255) NOT NULL,
    rol VARCHAR(55) NOT NULL,
    salarioMensual DOUBLE(10,2),
    activo BOOLEAN NOT NULL,
    equipo_id BIGINT NOT NULL,
    FOREIGN KEY (equipo_id) REFERENCES equipo(id)
);

CREATE TABLE roster_historico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    idJugador BIGINT NOT NULL,
    nickname VARCHAR(55) NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFin DATE,
    equipo_id BIGINT NOT NULL,

    FOREIGN KEY (equipo_id) REFERENCES equipo(id)
);