package com.esports.ms_torneos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.ms_torneos.model.Torneo;
import java.util.List;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {

    List<Torneo> findByIdJuego(Long idJuego);

    List<Torneo> findByEstado(String estado);

    List<Torneo> findByOrganizador(String organizador);
}
