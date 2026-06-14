package com.esports.ms_jugadores.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JugadorResponse {
    private Long id;
    private String nickname;
    private String nombreReal;
    private String pais;
    private LocalDate fechaNacimiento;
    private String rol;
    private Long idEquipoActual;
    private Boolean activo;
    private Double salarioMensual;
}
