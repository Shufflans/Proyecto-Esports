package com.esports.ms_juegos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.ms_juegos.model.Juego;
import java.util.List;

public interface JuegoRepository extends JpaRepository<Juego, Long> {

    Optional<Juego> findByNombreJuego(String nombreJuego);

    boolean existsByNombreJuego(String nombreJuego);

    List<Juego> findByGeneroJuego(String generoJuego);

    List<Juego> findByActivoTrue();
}
