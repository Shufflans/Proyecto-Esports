package com.esports.ms_patrocinadores.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.esports.ms_patrocinadores.client.EquipoClient;
import com.esports.ms_patrocinadores.dto.EquipoResponse;
import com.esports.ms_patrocinadores.dto.PatrocinadorRequest;
import com.esports.ms_patrocinadores.dto.PatrocinadorResponse;
import com.esports.ms_patrocinadores.exception.EquipoNoEncontradoException;
import com.esports.ms_patrocinadores.model.Patrocinador;
import com.esports.ms_patrocinadores.repository.PatrocinadorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatrocinadorService {

    private final PatrocinadorRepository patrocinadorRepository;
    private final EquipoClient equipoClient;

    @Transactional
    public PatrocinadorResponse registrarPatrocinador(PatrocinadorRequest pr, String token) {
        log.info("Registrando patrocinador para el equipo ID: {}", pr.getIdEquipo());

        EquipoResponse equipoRemoto = equipoClient.obtenerEquipo(pr.getIdEquipo(), token);

        if (equipoRemoto == null) {
            log.warn("El equipo con ID: {} no existe", pr.getIdEquipo());
            throw new EquipoNoEncontradoException("Equipo con la ID: " + pr.getIdEquipo() + " no encontrado");
        }

        Patrocinador patrocinadorNuevo = new Patrocinador();

        patrocinadorNuevo.setNombreMarca(pr.getNombreMarca());
        patrocinadorNuevo.setIdEquipo(pr.getIdEquipo());
        patrocinadorNuevo.setMontoAnual(pr.getMontoAnual());
        patrocinadorNuevo.setFechaInicio(pr.getFechaInicio());
        patrocinadorNuevo.setFechaFin(pr.getFechaFin());

        patrocinadorRepository.save(patrocinadorNuevo);

        return mapearAPatrocinador(patrocinadorNuevo);
    }

    @Transactional
    public List<PatrocinadorResponse> listarTodosLosPatrocinadores() {
        log.info("Listando todos los patrocinadores");

        List<PatrocinadorResponse> listaPatrocinadores = patrocinadorRepository.findAll().stream()
                .map(this::mapearAPatrocinador)
                .toList();

        return listaPatrocinadores;
    }

    @Transactional
    public List<PatrocinadorResponse> buscarPatrocinadoresPorEquipo(Long idEquipo) {
        log.info("Listando patrocinadores del equipo con ID: {}", idEquipo);

        List<PatrocinadorResponse> listaPorEquipo = patrocinadorRepository.findByIdEquipo(idEquipo).stream()
                .map(this::mapearAPatrocinador)
                .toList();

        return listaPorEquipo;
    }

    private PatrocinadorResponse mapearAPatrocinador(Patrocinador p) {
        return PatrocinadorResponse.builder()
                .id(p.getId())
                .nombreMarca(p.getNombreMarca())
                .idEquipo(p.getIdEquipo())
                .montoAnual(p.getMontoAnual())
                .fechaInicio(p.getFechaInicio())
                .fechaFin(p.getFechaFin())
                .build();
    }
}