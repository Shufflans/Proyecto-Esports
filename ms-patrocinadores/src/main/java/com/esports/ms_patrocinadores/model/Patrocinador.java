package com.esports.ms_patrocinadores.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patrocinadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patrocinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_marca", nullable = false)
    private String nombreMarca;

    @Column(name = "nombre_equipo", nullable = false)
    private Long idEquipo;

    private Double montoAnual;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
