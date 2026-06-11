package com.esports.ms_jugadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.ms_jugadores.model.Jugador;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {

}
