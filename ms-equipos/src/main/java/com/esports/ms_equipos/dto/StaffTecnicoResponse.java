package com.esports.ms_equipos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffTecnicoResponse {
    private Long id;
    private String nombreStaff;
    private String rol;
    private Double salarioMensual;
    private Boolean activo;
    private Long equipoId;
}
