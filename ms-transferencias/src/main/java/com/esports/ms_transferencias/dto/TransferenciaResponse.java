package com.esports.ms_transferencias.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferenciaResponse {
    private Long id;
    private Long idJugador;
    private String nicknameJugador;
    private Long idEquipoOrigen;
    private String nombreEquipoOrigen;
    private Long idEquipoDestino;
    private String nombreEquipoDestino;
    private LocalDate fechaTransferencia;
    private Double montoUsd;
    private String tipo;
    private Integer duracionContratoMeses;
    private String observaciones;
}