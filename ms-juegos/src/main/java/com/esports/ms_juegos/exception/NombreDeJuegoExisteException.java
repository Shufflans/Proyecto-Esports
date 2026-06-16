package com.esports.ms_juegos.exception;

public class NombreDeJuegoExisteException extends RuntimeException {
    public NombreDeJuegoExisteException(String mensaje) {
        super(mensaje);
    }
}
