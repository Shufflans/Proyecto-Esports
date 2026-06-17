package com.esports.ms_partidas.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidaRequest {

    @NotNull(message = "El ID del torneo es obligatorio")
    private Long idTorneo;

    @NotNull(message = "El ID del equipo local es obligatorio")
    private Long idEquipoLocal;

    @NotNull(message = "El ID del equipo visitante es obligatorio")
    private Long idEquipoVisitante;

    @NotNull(message = "La fecha y hora exacta es obligatoria")
    private LocalDateTime fechaHora;
}
