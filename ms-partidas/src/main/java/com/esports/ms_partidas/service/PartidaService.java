package com.esports.ms_partidas.service;

import org.springframework.stereotype.Service;

import com.esports.ms_partidas.client.EquipoClient;
import com.esports.ms_partidas.client.TorneoClient;
import com.esports.ms_partidas.dto.EquipoResponse;
import com.esports.ms_partidas.dto.PartidaRequest;
import com.esports.ms_partidas.dto.PartidaResponse;
import com.esports.ms_partidas.dto.ResultadoPartidaRequest;
import com.esports.ms_partidas.dto.TorneoResponse;
import com.esports.ms_partidas.exception.EquiposDesactivadosException;
import com.esports.ms_partidas.exception.EquiposNoExistenException;
import com.esports.ms_partidas.exception.GanadorInvalidoException;
import com.esports.ms_partidas.exception.MismoEquipoException;
import com.esports.ms_partidas.exception.PartidaNoExisteException;
import com.esports.ms_partidas.exception.PartidaYaFinalizadaException;
import com.esports.ms_partidas.exception.TorneoNoExisteException;
import com.esports.ms_partidas.model.Partida;
import com.esports.ms_partidas.repository.PartidaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartidaService {

    private final PartidaRepository partidaRepository;

    private final TorneoClient torneoClient;

    private final EquipoClient equipoClient;

    @Transactional
    public PartidaResponse crearPartida(PartidaRequest pr, String token) {
        log.info("Creando la partida");

        TorneoResponse torneo = torneoClient.obtenerTorneo(pr.getIdTorneo(), token);
        EquipoResponse equipoLocal = equipoClient.obtenerEquipo(pr.getIdEquipoLocal(), token);
        EquipoResponse equipoVisita = equipoClient.obtenerEquipo(pr.getIdEquipoVisitante(), token);

        if (torneo == null) {
            log.warn("El torneo con ID: {} NO EXISTE", pr.getIdTorneo());
            throw new TorneoNoExisteException("El torneo con el ID " + pr.getIdTorneo() + " no existe");
        }

        if (equipoLocal == null || equipoVisita == null) {
            log.warn("El equipo visitante o local no existe");
            throw new EquiposNoExistenException("El Equipo Local o Visitante no existen");
        }

        if (equipoLocal.getId().equals(equipoVisita.getId())) {
            log.warn("El equipo visitante y local no pueden ser el mismo");
            throw new MismoEquipoException("El equipo visitante y local no pueden ser el mismo");
        }

        if (equipoLocal.getActivo() == false || equipoVisita.getActivo() == false) {
            log.warn("El equipo visitante o local están desactivados");
            throw new EquiposDesactivadosException("El Equipo Local o Visitante están desactivados");
        }

        Partida partidaNueva = new Partida();

        partidaNueva.setIdTorneo(torneo.getId());
        partidaNueva.setNombreTorneo(torneo.getNombre());
        partidaNueva.setIdEquipoLocal(equipoLocal.getId());
        partidaNueva.setNombreEquipoLocal(equipoLocal.getNombre());
        partidaNueva.setIdEquipoVisitante(equipoVisita.getId());
        partidaNueva.setNombreEquipoVisitante(equipoVisita.getNombre());
        partidaNueva.setFechaHora(pr.getFechaHora());
        partidaNueva.setEstado("PROGRAMADA");

        partidaRepository.save(partidaNueva);

        return mapearAPartidaResponse(partidaNueva);
    }

    @Transactional
    public PartidaResponse finalizarPartida(Long id, ResultadoPartidaRequest rpr) {
        log.info("Finalizando la partida con ID: {}", id);

        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("La partida con ID: {} NO EXISTE", id);
                    throw new PartidaNoExisteException("La partida con el ID " + id + " no existe");
                });

        if ("TERMINADA".equals(partida.getEstado())) {
            log.warn("La partida con ID: {} ya se encuentra finalizada", id);
            throw new PartidaYaFinalizadaException("Esta partida ya esta terminada y no puede ser modificada");
        }

        if (!rpr.getIdEquipoGanador().equals(partida.getIdEquipoLocal()) &&
                !rpr.getIdEquipoGanador().equals(partida.getIdEquipoVisitante())) {
            log.warn("El equipo ganador con ID: {} no pertenece a los equipos de la partida", rpr.getIdEquipoGanador());
            throw new GanadorInvalidoException("El equipo ganador debe ser el local o el visitante");
        }

        partida.setMarcadorLocal(rpr.getMarcadorLocal());
        partida.setMarcadorVisitante(rpr.getMarcadorVisitante());
        partida.setDuracionMinutos(rpr.getDuracionMinutos());
        partida.setIdEquipoGanador(rpr.getIdEquipoGanador());
        partida.setEstado("TERMINADA");

        partidaRepository.save(partida);

        return mapearAPartidaResponse(partida);
    }

    public PartidaResponse obtenerPorId(Long id) {
        log.info("Buscando partida con ID: {}", id);

        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("La partida con ID: {} NO EXISTE", id);
                    throw new PartidaNoExisteException("La partida con el ID " + id + " no existe");
                });

        return mapearAPartidaResponse(partida);
    }

    public java.util.List<PartidaResponse> listarTodas() {
        log.info("Listando todas las partidas");

        return partidaRepository.findAll().stream()
                .map(this::mapearAPartidaResponse)
                .toList();
    }

    private PartidaResponse mapearAPartidaResponse(Partida p) {
        return PartidaResponse.builder()
                .id(p.getId())
                .idTorneo(p.getIdTorneo())
                .nombreTorneo(p.getNombreTorneo())
                .idEquipoLocal(p.getIdEquipoLocal())
                .nombreEquipoLocal(p.getNombreEquipoLocal())
                .idEquipoVisitante(p.getIdEquipoVisitante())
                .nombreEquipoVisitante(p.getNombreEquipoVisitante())
                .fechaHora(p.getFechaHora())
                .duracionMinutos(p.getDuracionMinutos())
                .marcadorLocal(p.getMarcadorLocal())
                .marcadorVisitante(p.getMarcadorVisitante())
                .idEquipoGanador(p.getIdEquipoGanador())
                .estado(p.getEstado())
                .build();
    }
}
