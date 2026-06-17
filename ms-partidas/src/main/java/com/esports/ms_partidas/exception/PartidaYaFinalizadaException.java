package com.esports.ms_partidas.exception;

public class PartidaYaFinalizadaException extends RuntimeException {
    public PartidaYaFinalizadaException(String mensaje) {
        super(mensaje);
    }
}
