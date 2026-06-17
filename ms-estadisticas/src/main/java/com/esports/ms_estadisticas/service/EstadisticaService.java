package com.esports.ms_estadisticas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esports.ms_estadisticas.client.JugadorClient;
import com.esports.ms_estadisticas.client.PartidaClient;
import com.esports.ms_estadisticas.dto.EstadisticaRequest;
import com.esports.ms_estadisticas.dto.EstadisticaResponse;
import com.esports.ms_estadisticas.dto.JugadorResponse;
import com.esports.ms_estadisticas.dto.PartidaResponse;
import com.esports.ms_estadisticas.exception.EstadisticaYaExisteException;
import com.esports.ms_estadisticas.exception.JugadorNoEncontradoException;
import com.esports.ms_estadisticas.exception.PartidaNoEncontradaException;
import com.esports.ms_estadisticas.model.Estadistica;
import com.esports.ms_estadisticas.repository.EstadisticaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EstadisticaService {

    private final EstadisticaRepository estadisticaRepository;
    private final JugadorClient jugadorClient;
    private final PartidaClient partidaClient;

    @Transactional
    public EstadisticaResponse crearEstadistica(EstadisticaRequest er, String token) {
        log.info("Creando estadística para el jugador ID: {} en la partida ID: {}", er.getIdJugador(),
                er.getIdPartida());

        if (estadisticaRepository.existsByIdJugadorAndIdPartida(er.getIdJugador(), er.getIdPartida())) {
            log.warn("La estadística para el jugador ID: {} en la partida ID: {} ya existe", er.getIdJugador(),
                    er.getIdPartida());
            throw new EstadisticaYaExisteException(
                    "El jugador con ID " + er.getIdJugador() + " ya tiene estadísticas registradas para la partida "
                            + er.getIdPartida());
        }

        JugadorResponse jugador = jugadorClient.obtenerJugador(er.getIdJugador(), token);
        if (jugador == null) {
            log.warn("El jugador con ID: {} no existe", er.getIdJugador());
            throw new JugadorNoEncontradoException("Jugador con la ID: " + er.getIdJugador() + " no encontrado");
        }

        PartidaResponse partida = partidaClient.obtenerPartida(er.getIdPartida(), token);
        if (partida == null) {
            log.warn("La partida con ID: {} no existe", er.getIdPartida());
            throw new PartidaNoEncontradaException("Partida con la ID: " + er.getIdPartida() + " no encontrada");
        }

        double divisorMuertes;

        if (er.getMuertes() == 0) {
            divisorMuertes = 1.0;
        } else {
            divisorMuertes = er.getMuertes();
        }

        double kda = (er.getAsesinatos() + er.getAsistencias()) / divisorMuertes;
        kda = Math.round(kda * 100.0) / 100.0;

        Estadistica crearEstadistica = Estadistica.builder()
                .idJugador(er.getIdJugador())
                .nicknameJugador(jugador.getNickname())
                .idPartida(er.getIdPartida())
                .asesinatos(er.getAsesinatos())
                .muertes(er.getMuertes())
                .asistencias(er.getAsistencias())
                .kda(kda)
                .mvp(er.getMvp())
                .fechaRegistro(LocalDateTime.now())
                .build();

        Estadistica estadisticaGuardada = estadisticaRepository.save(crearEstadistica);

        return mapearAEstadisticaResponse(estadisticaGuardada);
    }

    @Transactional
    public List<EstadisticaResponse> listarEstadisticasPorJugador(Long idJugador) {
        log.info("Listando las estadísticas por jugador ID: {}", idJugador);

        List<EstadisticaResponse> estadisticasPorJugador = estadisticaRepository.findByIdJugador(idJugador).stream()
                .map(this::mapearAEstadisticaResponse)
                .toList();

        return estadisticasPorJugador;
    }

    @Transactional
    public List<EstadisticaResponse> listarEstadisticasPorPartida(Long idPartida) {
        log.info("Listando las estadísticas por partida ID: {}", idPartida);

        List<EstadisticaResponse> estadisticasPorPartida = estadisticaRepository.findByIdPartida(idPartida).stream()
                .map(this::mapearAEstadisticaResponse)
                .toList();

        return estadisticasPorPartida;
    }

    private EstadisticaResponse mapearAEstadisticaResponse(Estadistica e) {
        return EstadisticaResponse.builder()
                .id(e.getId())
                .idJugador(e.getIdJugador())
                .idPartida(e.getIdPartida())
                .nicknameJugador(e.getNicknameJugador())
                .asesinatos(e.getAsesinatos())
                .muertes(e.getMuertes())
                .asistencias(e.getAsistencias())
                .kda(e.getKda())
                .mvp(e.getMvp())
                .fechaRegistro(e.getFechaRegistro())
                .build();
    }
}