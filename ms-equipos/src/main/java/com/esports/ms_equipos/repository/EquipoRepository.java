package com.esports.ms_equipos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.ms_equipos.model.Equipo;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    Optional<Equipo> findByNombreEquipoAndRegion(String nombreEquipo, String region);

    Boolean existsByNombreEquipoAndRegion(String nombreEquipo, String region);

    List<Equipo> findByRegion(String region);

    List<Equipo> findByActivoTrue();

    Optional<Equipo> findById(Long id);

    Boolean existsByNombreEquipoAndRegionAndIdNot(String nombreEquipo, String region, Long id);

}
