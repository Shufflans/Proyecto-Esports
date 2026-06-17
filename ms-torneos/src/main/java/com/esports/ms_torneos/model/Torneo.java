package com.esports.ms_torneos.model;

import java.time.LocalDate;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "torneos")
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_torneo", nullable = false)
    private String nombreTorneo;

    @Column(name = "id_juego", nullable = false)
    private Long idJuego;

    @Column(name = "nombre_juego", nullable = false)
    private String nombreJuego;

    @Column(name = "organizador", nullable = false)
    private String organizador;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "total_premio", nullable = false)
    private Double totalPremio;

    @Column(name = "max_equipos", nullable = false)
    private Integer maxEquipos;

    @Column(name = "estado", nullable = false)
    private String estado;
}
