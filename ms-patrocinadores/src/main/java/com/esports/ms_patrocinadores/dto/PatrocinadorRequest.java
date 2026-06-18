package com.esports.ms_patrocinadores.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatrocinadorRequest {

    @NotBlank(message = "El nombre de la marca no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre de la marca debe tener entre 2 y 100 caracteres")
    private String nombreMarca;

    @NotNull(message = "El ID del equipo es obligatorio")
    @Positive(message = "El ID del equipo debe ser un número positivo")
    private Long idEquipo;

    @NotNull(message = "El monto anual es obligatorio")
    @PositiveOrZero(message = "El monto no puede ser negativo")
    private Double montoAnual;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Future(message = "La fecha de fin debe ser una fecha futura")
    private LocalDate fechaFin;
}