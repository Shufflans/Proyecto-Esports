package com.esports.ms_equipos.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "equipo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "nombre_equipo")
    private String nombreEquipo;

    @Column(nullable = false, name = "region")
    private String region;

    @Column(nullable = false, name = "fecha_fundacion")
    private LocalDate fechaFundacion;

    private Integer rankingMundial;

    @Column(nullable = false, name = "activo")
    private Boolean activo;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StaffTecnico> staff = new ArrayList<>();

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RosterHistorico> rosterHistorico = new ArrayList<>();

    public void agregarStaff(StaffTecnico st) {
        staff.add(st);
        st.setEquipo(this);
    }

    public void agregarRoster(RosterHistorico rh) {
        rosterHistorico.add(rh);
        rh.setEquipo(this);
    }

}
