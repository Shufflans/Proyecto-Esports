package com.esports.ms_torneos.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TorneoResponse {
    private Long id;
    private Long idJuego;
    private String nombreTorneo;
    private String nombreJuego;
    private String organizador;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double totalPremio;
    private Integer maxEquipos;
    private String estado;
}
