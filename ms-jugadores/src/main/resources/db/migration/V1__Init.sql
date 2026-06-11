CREATE TABLE jugadores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(55) NOT NULL UNIQUE,
    nombre_real VARCHAR(50) NOT NULL,
    pais VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    rol VARCHAR(20) NOT NULL,
    id_equipo_actual BIGINT,
    activo BOOLEAN NOT NULL,
    salario_mensual DOUBLE NOT NULL
);