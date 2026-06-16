package com.esports.ms_equipos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipoRequest {

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 carácteres")
    private String nombreEquipo;

    @NotBlank(message = "La región del equipo es obligatoria")
    private String region;

    @NotNull(message = "La fecha de fundación es obligatoria")
    private LocalDate fechaFundacion;

    private Integer rankingMundial;

    private Boolean activo;
}
