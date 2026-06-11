package com.esports.ms_equipos.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RosterHistoricoResponse {
    private Long id;
    private Long idJugador;
    private String nickname;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long equipoId;
}
