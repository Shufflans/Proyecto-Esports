package com.esports.ms_torneos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class TorneoRequest {

    @NotNull(message = "El ID del juego es obligatorio")
    private Long idJuego;

    @NotBlank(message = "El nombre del torneo es obligatorio")
    private String nombreTorneo;

    @NotBlank(message = "El nombre del organizador es obligatorio")
    private String organizador;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @NotNull(message = "Total del premio del torneo es obligatorio")
    @PositiveOrZero(message = "El premio no puede ser negativo")
    private Double totalPremio;

    @NotNull(message = "La cantidad de equipos es obligatoria")
    @Min(2)
    private Integer maxEquipos;
}
