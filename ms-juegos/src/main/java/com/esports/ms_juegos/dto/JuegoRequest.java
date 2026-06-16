package com.esports.ms_juegos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuegoRequest {

    @NotBlank(message = "El nombre del juego es obligatorio")
    @Size(min = 2, max = 20, message = "El nombre del juego debe tener entre 2 y 20 caracteres")
    private String nombreJuego;

    @NotBlank(message = "El género del juego es obligatorio")
    @Size(min = 2, max = 20, message = "El género debe tener entre 2 y 20 caracteres")
    private String generoJuego;

    @NotNull(message = "El total de premio es obligatorio")
    @PositiveOrZero(message = "El monto del permio no puede ser negativo")
    private Double totalPremio;
}
