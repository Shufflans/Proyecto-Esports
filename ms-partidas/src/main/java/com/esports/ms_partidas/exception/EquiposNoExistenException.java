package com.esports.ms_partidas.exception;

public class EquiposNoExistenException extends RuntimeException {
    public EquiposNoExistenException(String mensaje) {
        super(mensaje);
    }
}
