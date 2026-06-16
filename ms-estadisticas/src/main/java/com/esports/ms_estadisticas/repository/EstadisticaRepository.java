package com.esports.ms_estadisticas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.ms_estadisticas.model.Estadistica;
import java.util.List;

public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {
    List<Estadistica> findByIdJugador(Long idJugador);

    List<Estadistica> findByIdPartida(Long idPartida);

    Boolean existsByIdJugadorAndIdPartida(Long idJugador, Long idPartida);
}
