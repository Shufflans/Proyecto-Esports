package com.esports.ms_notificaciones.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionResponse {
    private Long id;
    private Long idJugador;
    private String nombreJugador;
    private String asunto;
    private String mensaje;
    private String estado;
    private LocalDateTime fechaCreacion;
}
