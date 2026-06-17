package com.esports.ms_transferencias.exception;

public class JugadorNoExisteException extends RuntimeException {
    public JugadorNoExisteException(String mensaje) {
        super(mensaje);
    }
}
