package com.esports.ms_transferencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.ms_transferencias.model.Transferencia;
import java.util.List;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByIdJugador(Long idJugador);

    List<Transferencia> findByTipo(String tipo);
}
