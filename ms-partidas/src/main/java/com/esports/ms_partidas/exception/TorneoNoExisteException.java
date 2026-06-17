package com.esports.ms_partidas.exception;

public class TorneoNoExisteException extends RuntimeException {
    public TorneoNoExisteException(String mensaje) {
        super(mensaje);
    }
}
