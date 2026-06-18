package com.esports.ms_rankings.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rankings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_jugador", nullable = false)
    private Long idJugador;

    @Column(name = "nombre_jugador", nullable = false)
    private String nombreJugador;

    @Column(name = "id_equipo", nullable = false)
    private Long idEquipo;

    @Column(name = "nombre_equipo", nullable = false)
    private String nombreEquipo;

    @Column(name = "puntos", nullable = false)
    private Integer puntos;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

}