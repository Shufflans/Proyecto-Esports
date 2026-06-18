package com.esports.ms_rankings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingResponse {

    private Long id;
    private Long idJugador;
    private String nombreJugador;
    private Long idEquipo;
    private String nombreEquipo;
    private Integer puntos;
    private LocalDateTime fechaActualizacion;

}