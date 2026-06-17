package com.esports.ms_partidas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoPartidaRequest {

    @NotNull(message = "El marcador del equipo local es obligatorio")
    @PositiveOrZero(message = "El marcador del equipo local no puede ser negativo")
    private Integer marcadorLocal;

    @NotNull(message = "El marcador del equipo visitante es obligatorio")
    @PositiveOrZero(message = "El marcador del equipo visitante no puede ser negativo")
    private Integer marcadorVisitante;

    @NotNull(message = "La duración de la partida es obligatoria")
    @PositiveOrZero(message = "La duración de la partida debe ser al menos de 1 minuto")
    private Integer duracionMinutos;

    @NotNull(message = "El ID del equipo ganador es obligatorio")
    private Long idEquipoGanador;
}