package com.esports.ms_torneos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JuegoResponse {
    private Long idJuego;
    private String nombreJuego;
    private Boolean activo;
}
