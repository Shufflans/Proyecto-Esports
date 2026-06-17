package com.esports.ms_estadisticas.exception;

public class EstadisticaYaExisteException extends RuntimeException {
    public EstadisticaYaExisteException(String mensaje) {
        super(mensaje);
    }
}
