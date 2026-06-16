package com.esports.ms_estadisticas.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estadisticas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_jugador", nullable = false)
    private Long idJugador;

    @Column(name = "nickname_jugador", nullable = false)
    private String nicknameJugador;

    @Column(name = "id_partida", nullable = false)
    private Long idPartida;

    @Column(name = "asesinatos", nullable = false)
    private Integer asesinatos;

    @Column(name = "muertes", nullable = false)
    private Integer muertes;

    @Column(name = "asistencias", nullable = false)
    private Integer asistencias;

    private Double kda;

    @Column(name = "mvp", nullable = false)
    private Boolean mvp;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
}
