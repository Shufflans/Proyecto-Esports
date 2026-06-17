package com.esports.ms_partidas.model;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "partidas")
@Builder
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_torneo", nullable = false)
    private Long idTorneo;

    private String nombreTorneo;

    @Column(name = "id_equipo_local", nullable = false)
    private Long idEquipoLocal;

    private String nombreEquipoLocal;

    @Column(name = "id_equipo_visitante", nullable = false)
    private Long idEquipoVisitante;

    private String nombreEquipoVisitante;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    private Integer duracionMinutos;

    private Integer marcadorLocal;

    private Integer marcadorVisitante;

    private Long idEquipoGanador;

    @Column(name = "estado", nullable = false)
    private String estado;
}
