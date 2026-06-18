package com.esports.ms_rankings.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esports.ms_rankings.client.EquipoClient;
import com.esports.ms_rankings.client.JugadorClient;
import com.esports.ms_rankings.dto.EquipoResponse;
import com.esports.ms_rankings.dto.JugadorResponse;
import com.esports.ms_rankings.dto.RankingRequest;
import com.esports.ms_rankings.dto.RankingResponse;
import com.esports.ms_rankings.exception.EquipoNoEncontradoException;
import com.esports.ms_rankings.exception.JugadorNoEncontradoException;
import com.esports.ms_rankings.exception.RankingNoEncontradoException;
import com.esports.ms_rankings.model.Ranking;
import com.esports.ms_rankings.repository.RankingRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankingService {

    private final RankingRepository rankingRepository;
    private final JugadorClient jugadorClient;
    private final EquipoClient equipoClient;

    @Transactional
    public RankingResponse registrarRanking(RankingRequest rr, String token) {
        log.info("Registrando o actualizando ranking para jugador ID: {}", rr.getIdJugador());

        JugadorResponse jugadorEncontrado = jugadorClient.obtenerJugador(rr.getIdJugador(), token);
        if (jugadorEncontrado == null) {
            log.warn("No existe un jugador con el ID: {}", rr.getIdJugador());
            throw new JugadorNoEncontradoException("El jugador con la ID: " + rr.getIdJugador() + " no existe");
        }

        EquipoResponse equipoEncontrado = equipoClient.obtenerEquipo(rr.getIdEquipo(), token);
        if (equipoEncontrado == null) {
            log.warn("No existe un equipo con el ID: {}", rr.getIdEquipo());
            throw new EquipoNoEncontradoException("El equipo con la ID: " + rr.getIdEquipo() + " no existe");
        }

        List<Ranking> rankingsPrevios = rankingRepository.findByIdJugador(rr.getIdJugador());

        Ranking ranking;

        if (!rankingsPrevios.isEmpty()) {
            ranking = rankingsPrevios.get(0);
            log.info("Ranking existente encontrado, actualizando puntos...");
        } else {
            ranking = new Ranking();
            log.info("Creando nuevo registro de ranking...");
        }

        ranking.setIdJugador(rr.getIdJugador());
        ranking.setNombreJugador(jugadorEncontrado.getNickname());
        ranking.setIdEquipo(rr.getIdEquipo());
        ranking.setNombreEquipo(equipoEncontrado.getNombre());
        ranking.setPuntos(rr.getPuntos());
        ranking.setFechaActualizacion(LocalDateTime.now());

        rankingRepository.save(ranking);

        return mapearARanking(ranking);
    }

    @Transactional
    public List<RankingResponse> listarTodosLosRankings() {
        log.info("Listando todos los rankings");

        List<RankingResponse> listaRankings = rankingRepository.findAll().stream()
                .map(this::mapearARanking)
                .toList();

        return listaRankings;
    }

    @Transactional
    public List<RankingResponse> listarRankingsPorJugador(Long idJugador) {
        log.info("Listando rankings para el jugador ID: {}", idJugador);

        List<RankingResponse> listaRankings = rankingRepository.findByIdJugador(idJugador).stream()
                .map(this::mapearARanking)
                .toList();

        if (listaRankings.isEmpty()) {
            log.warn("No se encontraron rankings para el jugador ID: {}", idJugador);
            throw new RankingNoEncontradoException("No hay rankings registrados para el jugador con ID: " + idJugador);
        }

        return listaRankings;
    }

    @Transactional
    public List<RankingResponse> listarRankingsPorEquipo(Long idEquipo) {
        log.info("Listando rankings para el equipo ID: {}", idEquipo);

        List<RankingResponse> listaRankings = rankingRepository.findByIdEquipo(idEquipo).stream()
                .map(this::mapearARanking)
                .toList();

        if (listaRankings.isEmpty()) {
            log.warn("No se encontraron rankings para el equipo ID: {}", idEquipo);
            throw new RankingNoEncontradoException("No hay rankings registrados para el equipo con ID: " + idEquipo);
        }

        return listaRankings;
    }

    @Transactional
    public void eliminarRanking(Long id) {
        log.info("Eliminando el ranking con ID: {}", id);

        Ranking ranking = rankingRepository.findById(id).orElseThrow(() -> {
            log.warn("No se ha encontrado el Ranking con la ID: {}", id);
            throw new RankingNoEncontradoException("Ranking con la ID: " + id + " no encontrado.");
        });

        rankingRepository.delete(ranking);
    }

    private RankingResponse mapearARanking(Ranking r) {
        return RankingResponse.builder()
                .id(r.getId())
                .idJugador(r.getIdJugador())
                .nombreJugador(r.getNombreJugador())
                .idEquipo(r.getIdEquipo())
                .nombreEquipo(r.getNombreEquipo())
                .puntos(r.getPuntos())
                .fechaActualizacion(r.getFechaActualizacion())
                .build();
    }
}