package com.esports.ms_transferencias.model;

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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transferencias")
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_jugador", nullable = false)
    private Long idJugador;

    private String nicknameJugador;

    private Long idEquipoOrigen;

    private String nombreEquipoOrigen;

    private Long idEquipoDestino;

    private String nombreEquipoDestino;

    @Column(name = "fecha_transferencia", nullable = false)
    private LocalDate fechaTransferencia;

    private Double montoUsd;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    private Integer duracionContratoMeses;

    private String observaciones;
}
