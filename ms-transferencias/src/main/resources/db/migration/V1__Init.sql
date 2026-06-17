CREATE TABLE transferencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_jugador BIGINT NOT NULL,
    nickname_jugador VARCHAR(255),
    id_equipo_origen BIGINT,
    nombre_equipo_origen VARCHAR(255),
    id_equipo_destino BIGINT,
    nombre_equipo_destino VARCHAR(255),
    fecha_transferencia DATE NOT NULL,
    monto_usd DOUBLE,
    tipo VARCHAR(255) NOT NULL,
    duracion_contrato_meses INT,
    observaciones VARCHAR(255)
);