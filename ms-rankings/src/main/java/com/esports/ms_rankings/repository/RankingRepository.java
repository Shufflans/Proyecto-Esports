package com.esports.ms_rankings.repository;

import com.esports.ms_rankings.model.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    List<Ranking> findByIdJugador(Long idJugador);

    List<Ranking> findByIdEquipo(Long idEquipo);
}