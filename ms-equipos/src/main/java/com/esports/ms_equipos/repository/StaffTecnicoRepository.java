package com.esports.ms_equipos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.ms_equipos.model.StaffTecnico;

public interface StaffTecnicoRepository extends JpaRepository<StaffTecnico, Long> {
    List<StaffTecnico> findByEquipoId(Long id);

    List<StaffTecnico> findByEquipoIdAndRolAndActivoTrue(Long id, String rol);

}
