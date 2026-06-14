package com.esports.ms_equipos.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipoResponse {
    private Long id;
    private String nombreEquipo;
    private String region;
    private LocalDate fechaFundacion;
    private Integer rankingMundial;
    private Boolean activo;
    private Integer cantidadStaff;
    private Integer cantidadRoster;
}
