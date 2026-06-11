package com.esports.ms_jugadores.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.ms_jugadores.model.Jugador;
import java.util.List;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    Optional<Jugador> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    List<Jugador> findByPais(String pais);

    List<Jugador> findByIdEquipoActual(Long idEquipoActual);

    List<Jugador> findByActivoTrue();

    List<Jugador> findByPais(String pais);
}
