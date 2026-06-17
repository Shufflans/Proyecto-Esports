package com.esports.ms_estadisticas.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidaResponse {
    private Long id;
    private Long idTorneo;
    private String nombreTorneo;
    private Long idEquipoLocal;
    private String nombreEquipoLocal;
    private Long idEquipoVisitante;
    private String nombreEquipoVisitante;
    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
    private Integer marcadorLocal;
    private Integer marcadorVisitante;
    private Long idEquipoGanador;
    private String estado;
}