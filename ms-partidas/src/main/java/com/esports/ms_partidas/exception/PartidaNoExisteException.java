package com.esports.ms_partidas.exception;

public class PartidaNoExisteException extends RuntimeException {
    public PartidaNoExisteException(String mensaje) {
        super(mensaje);
    }
}
