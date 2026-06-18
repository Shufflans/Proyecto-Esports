package com.esports.ms_notificaciones.exception;

public class NotificacionNoExisteException extends RuntimeException {
    public NotificacionNoExisteException(String mensaje) {
        super(mensaje);
    }
}
