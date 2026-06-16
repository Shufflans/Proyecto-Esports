package com.esports.ms_torneos.exception;

public class NoExisteJuegoConIdException extends RuntimeException {
    public NoExisteJuegoConIdException(String mensaje) {
        super(mensaje);
    }
}
