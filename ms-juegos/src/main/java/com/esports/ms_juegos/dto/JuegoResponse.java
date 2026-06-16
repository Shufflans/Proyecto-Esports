package com.esports.ms_juegos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuegoResponse {
    private Long id;
    private String nombreJuego;
    private String generoJuego;
    private Boolean activo;
}
