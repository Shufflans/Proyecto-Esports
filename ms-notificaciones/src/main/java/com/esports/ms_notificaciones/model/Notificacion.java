package com.esports.ms_notificaciones.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_jugador", nullable = false)
    private Long idJugador;

    @Column(name = "nombre_jugador", nullable = false)
    private String nombreJugador;

    @Column(name = "asunto", nullable = false)
    private String asunto;

    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}