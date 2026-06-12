package com.esports.ms_equipos.dto;

import jakarta.validation.constraints.NotBlank;
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
public class StaffTecnicoRequest {

    @NotBlank(message = "El nombre del staff es obligatorio")
    private String nombreStaff;

    @NotBlank(message = "El rol del staff es obligatorio")
    private String rol;

    @NotNull(message = "El salario es obligatorio")
    @Positive(message = "El salario no puede ser negativo")
    private Double salarioMensual;

    @NotNull(message = "El estado activo/inactivo es obligatorio")
    private Boolean activo;
}
