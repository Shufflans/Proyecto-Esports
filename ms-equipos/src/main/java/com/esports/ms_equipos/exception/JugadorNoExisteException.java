package com.esports.ms_equipos.exception;

public class JugadorNoExisteException extends RuntimeException {
    public JugadorNoExisteException(String mensaje) {
        super(mensaje);
    }
}
