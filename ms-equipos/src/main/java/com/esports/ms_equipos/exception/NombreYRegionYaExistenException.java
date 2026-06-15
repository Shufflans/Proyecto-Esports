package com.esports.ms_equipos.exception;

public class NombreYRegionYaExistenException extends RuntimeException {
    public NombreYRegionYaExistenException(String mensaje) {
        super(mensaje);
    }
}
