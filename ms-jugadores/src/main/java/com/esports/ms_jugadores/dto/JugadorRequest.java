package com.esports.ms_jugadores.dto;

import java.time.LocalDate;

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
public class JugadorRequest {

    @NotBlank(message = "El nickname del jugador es obligatorio")
    private String nickname;

    @NotBlank(message = "El nombre del jugador es obligatorio")
    private String nombreReal;

    @NotBlank(message = "El país del jugador es obligatorio")
    private String pais;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El rol del jugador es obligatorio")
    private String rol;

    private Long idEquipoActual;

    @NotNull(message = "El salario mensual es obligatorio")
    @Positive(message = "El salario no puede ser negativo")
    private Double salarioMensual;
}
