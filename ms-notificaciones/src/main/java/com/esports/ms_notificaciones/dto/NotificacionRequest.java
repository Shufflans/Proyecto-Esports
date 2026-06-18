package com.esports.ms_notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionRequest {

    @NotNull(message = "El ID del jugador es obligatorio")
    private Long idJugador;

    @NotBlank(message = "El asunto es obligatorio")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}