package com.esports.ms_equipos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "staff_tecnico")
@Entity
public class StaffTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Nombre_Empleado", nullable = false)
    private String nombreStaff;

    @Column(name = "Rol", nullable = false)
    private String rol;

    private Double salarioMensual;

    @Column(nullable = false, name = "Activo")
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    @JsonBackReference
    private Equipo equipo;

}
