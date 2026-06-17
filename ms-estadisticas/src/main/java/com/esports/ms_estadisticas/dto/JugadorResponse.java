package com.esports.ms_estadisticas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JugadorResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private String nickname;
    private String rol;
    private Long idEquipo;
    private Boolean activo;
}