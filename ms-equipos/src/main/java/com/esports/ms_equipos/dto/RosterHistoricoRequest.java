package com.esports.ms_equipos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RosterHistoricoRequest {

    @NotNull(message = "El id del jugador es obligatorio")
    @Positive(message = "La id no puede ser negativa")
    private Long idJugador;

    @NotNull(message = "La fecha de inicio del jugador es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

}
