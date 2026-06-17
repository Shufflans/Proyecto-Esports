package com.esports.ms_transferencias.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrasnferenciaRequest {

    @NotNull(message = "El ID del jugador es obligatorio")
    private Long idJugador;

    private Long idEquipoOrigen;

    private Long idEquipoDestino;

    @NotNull(message = "La fecha de transferencia es obligatoria")
    private LocalDate fechaTransferencia;

    @Positive(message = "El monto debe ser un valor positivo")
    private Double montoUsd;

    @NotNull(message = "El tipo de transferencia es obligatorio")
    private String tipo;

    private Integer duracionContratoMeses;

    private String observaciones;
}
