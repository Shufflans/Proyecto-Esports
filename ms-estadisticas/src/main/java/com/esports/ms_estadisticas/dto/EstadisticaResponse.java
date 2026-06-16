package com.esports.ms_estadisticas.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticaResponse {
    Long id;
    Long idJugador;
    Long idPartida;
    String nicknameJugador;
    Integer asesinatos;
    Integer muertes;
    Integer asistencias;
    Double kda;
    Boolean mvp;
    LocalDateTime fechaRegistro;
}
