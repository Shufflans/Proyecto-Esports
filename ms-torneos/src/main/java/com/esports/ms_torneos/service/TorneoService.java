package com.esports.ms_torneos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.esports.ms_torneos.client.JuegoClient;
import com.esports.ms_torneos.dto.JuegoResponse;
import com.esports.ms_torneos.dto.TorneoRequest;
import com.esports.ms_torneos.dto.TorneoResponse;
import com.esports.ms_torneos.exception.JuegoDesactivadoException;
import com.esports.ms_torneos.exception.NoExisteJuegoConIdException;
import com.esports.ms_torneos.exception.TorneoFinalizadoException;
import com.esports.ms_torneos.exception.TorneoNoEncontradoException;
import com.esports.ms_torneos.model.Torneo;
import com.esports.ms_torneos.repository.TorneoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final JuegoClient juegoClient;

    @Transactional
    public TorneoResponse crearTorneo(TorneoRequest tr, String token) {
        log.info("Creando torneo: {}", tr.getNombreTorneo());

        JuegoResponse juegoEncontrado = juegoClient.obtenerJuego(tr.getIdJuego(), token);

        if (juegoEncontrado == null) {
            log.warn("No existe un juego con el ID: {}", tr.getIdJuego());
            throw new NoExisteJuegoConIdException("El juego con la ID: " + tr.getIdJuego() + " no existe");
        }

        if (juegoEncontrado.getActivo() == false) {
            log.warn("El Juego con la ID: {} está desactivado", tr.getIdJuego());
            throw new JuegoDesactivadoException("El Juego con la ID: " + tr.getIdJuego() + " está desactivado");
        }

        Torneo torneoNuevo = new Torneo();

        torneoNuevo.setNombreTorneo(tr.getNombreTorneo());
        torneoNuevo.setIdJuego(tr.getIdJuego());
        torneoNuevo.setNombreJuego(juegoEncontrado.getNombreJuego());
        torneoNuevo.setOrganizador(tr.getOrganizador());
        torneoNuevo.setFechaInicio(tr.getFechaInicio());
        torneoNuevo.setFechaFin(tr.getFechaFin());
        torneoNuevo.setTotalPremio(tr.getTotalPremio());
        torneoNuevo.setMaxEquipos(tr.getMaxEquipos());
        torneoNuevo.setEstado("PROGRAMADO");

        torneoRepository.save(torneoNuevo);

        return mapearATorneo(torneoNuevo);
    }

    @Transactional
    public TorneoResponse buscarPorId(Long id) {
        log.info("Buscando torneo por la ID:{}", id);

        Torneo torneoEncontrado = torneoRepository.findById(id).orElseThrow(() -> {
            log.warn("No se ha encontrado el Torneo con la ID: {}", id);
            throw new TorneoNoEncontradoException("Torneo con la ID: " + id + " no encontrado.");
        });

        return mapearATorneo(torneoEncontrado);
    }

    @Transactional
    public List<TorneoResponse> listarTodosLosTorneos() {
        log.info("Listando todos los torneos");

        List<TorneoResponse> listaTorneos = torneoRepository.findAll().stream()
                .map(this::mapearATorneo)
                .toList();

        return listaTorneos;
    }

    @Transactional
    public List<TorneoResponse> listarTorneosPorEstado(String estado) {
        log.info("Listando todos los torneos por estado");

        List<TorneoResponse> listaTorneos = torneoRepository.findByEstado(estado).stream()
                .map(this::mapearATorneo)
                .toList();

        return listaTorneos;
    }

    @Transactional
    public List<TorneoResponse> listarTorneosPorIdJuego(Long id) {
        log.info("Listando todos los torneos por ID juego");

        List<TorneoResponse> listaTorneos = torneoRepository.findByIdJuego(id).stream()
                .map(this::mapearATorneo)
                .toList();

        return listaTorneos;
    }

    @Transactional
    public TorneoResponse actualizarTorneo(Long id, TorneoRequest tr) {
        log.info("Actualizando datos del torneo: {}", id);

        Torneo torneo = torneoRepository.findById(id).orElseThrow(() -> {
            log.warn("No se ha encontrado el Torneo con la ID: {}", id);
            throw new TorneoNoEncontradoException("Torneo con la ID: " + id + " no encontrado.");
        });

        if (torneo.getEstado().equals("FINALIZADO")) {
            log.warn("No puedes modificar un torneo ya finalizado");
            throw new TorneoFinalizadoException("No puedes modificar un torneo ya finalizado");
        }

        torneo.setNombreTorneo(tr.getNombreTorneo());
        torneo.setTotalPremio(tr.getTotalPremio());

        if (tr.getFechaFin() != null) {
            torneo.setFechaFin(tr.getFechaFin());
        }

        torneoRepository.save(torneo);

        return mapearATorneo(torneo);
    }

    @Transactional
    public void desactivarTorneo(Long id) {
        log.info("Desactivando el torneo");

        Torneo torneo = torneoRepository.findById(id).orElseThrow(() -> {
            log.warn("No se ha encontrado el Torneo con la ID: {}", id);
            throw new TorneoNoEncontradoException("Torneo con la ID: " + id + " no encontrado.");
        });

        torneo.setEstado("TERMINADO");
        torneoRepository.save(torneo);
    }

    private TorneoResponse mapearATorneo(Torneo t) {
        return TorneoResponse.builder()
                .id(t.getId())
                .idJuego(t.getIdJuego())
                .nombreTorneo(t.getNombreTorneo())
                .nombreJuego(t.getNombreJuego())
                .organizador(t.getOrganizador())
                .fechaInicio(t.getFechaInicio())
                .fechaFin(t.getFechaFin())
                .totalPremio(t.getTotalPremio())
                .maxEquipos(t.getMaxEquipos())
                .estado(t.getEstado())
                .build();

    }
}
