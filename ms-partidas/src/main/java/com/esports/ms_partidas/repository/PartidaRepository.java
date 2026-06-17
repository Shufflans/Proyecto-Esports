package com.esports.ms_partidas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.ms_partidas.model.Partida;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByIdTorneo(Long idTorneo);

    List<Partida> findByEstado(String estado);

    List<Partida> findByIdEquipoLocalOrIdEquipoVisitante(Long idLocal, Long idVisitante);
}
