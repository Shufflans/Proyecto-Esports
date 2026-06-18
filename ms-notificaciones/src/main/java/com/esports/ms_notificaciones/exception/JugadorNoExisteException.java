package com.esports.ms_notificaciones.exception;

public class JugadorNoExisteException extends RuntimeException {
    public JugadorNoExisteException(String mensaje) {
        super(mensaje);
    }
}
