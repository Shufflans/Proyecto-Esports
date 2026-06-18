package com.esports.ms_patrocinadores.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class PatrocinadorResponse {
    private Long id;
    private String nombreMarca;
    private Long idEquipo;
    private Double montoAnual;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}