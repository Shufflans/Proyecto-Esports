package com.esports.ms_equipos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.ms_equipos.model.RosterHistorico;

public interface RosterHistoricoRepository extends JpaRepository<RosterHistorico, Long> {
    List<RosterHistorico> findByEquipoId(Long id);

    List<RosterHistorico> findByEquipoIdAndFechaFinIsNull(Long id);

    List<RosterHistorico> findByIdJugador(Long idJugador);
}
