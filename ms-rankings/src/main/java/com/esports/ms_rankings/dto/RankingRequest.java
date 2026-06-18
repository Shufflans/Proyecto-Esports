package com.esports.ms_rankings.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingRequest {

    @NotNull(message = "El ID del jugador es obligatorio")
    private Long idJugador;

    @NotNull(message = "El ID del equipo es obligatorio")
    private Long idEquipo;

    @NotNull(message = "Los puntos son obligatorios")
    @PositiveOrZero(message = "Los puntos no pueden ser negativos")
    private Integer puntos;
}