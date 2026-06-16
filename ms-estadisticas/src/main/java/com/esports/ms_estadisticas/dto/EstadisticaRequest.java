package com.esports.ms_estadisticas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticaRequest {

    @NotNull(message = "El ID del jugador es obligatorio")
    private Long idJugador;

    @NotNull(message = "El ID de la partida es obligatorio")
    private Long idPartida;

    @NotNull(message = "El número de asesinatos es obligatorio")
    private Integer asesinatos;

    @NotNull(message = "El número de muertes es obligatorio")
    private Integer muertes;

    @NotNull(message = "El número de asistencias es obligatorio")
    private Integer asistencias;

    @NotNull(message = "Se debe indicar si el Jugador fue MVP o no")
    private Boolean mvp;
}
