package com.esports.ms_patrocinadores.repository;

import com.esports.ms_patrocinadores.model.Patrocinador;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatrocinadorRepository extends JpaRepository<Patrocinador, Long> {

    List<Patrocinador> findByIdEquipo(Long idEquipo);

}