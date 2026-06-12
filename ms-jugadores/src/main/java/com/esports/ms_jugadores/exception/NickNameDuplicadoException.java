package com.esports.ms_jugadores.exception;

public class NickNameDuplicadoException extends RuntimeException {
    public NickNameDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
