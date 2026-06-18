package com.esports.ms_notificaciones.service;

import org.springframework.stereotype.Service;

import com.esports.ms_notificaciones.client.JugadorClient;
import com.esports.ms_notificaciones.dto.JugadorResponse;
import com.esports.ms_notificaciones.dto.NotificacionRequest;
import com.esports.ms_notificaciones.dto.NotificacionResponse;
import com.esports.ms_notificaciones.exception.JugadorNoExisteException;
import com.esports.ms_notificaciones.exception.NotificacionNoExisteException;
import com.esports.ms_notificaciones.exception.NotificacionYaLeidaException;
import com.esports.ms_notificaciones.model.Notificacion;
import com.esports.ms_notificaciones.repository.NotificacionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    private final JugadorClient jugadorClient;

    @Transactional
    public NotificacionResponse crearNotificacion(NotificacionRequest nr, String token) {
        log.info("Creando notificación para el jugador ID: {}", nr.getIdJugador());

        JugadorResponse jugador = jugadorClient.obtenerJugador(nr.getIdJugador(), token);

        if (jugador == null) {
            log.warn("El jugador con ID: {} NO EXISTE", nr.getIdJugador());
            throw new JugadorNoExisteException("El jugador con el ID " + nr.getIdJugador() + " no existe");
        }

        Notificacion notificacionNueva = new Notificacion();

        notificacionNueva.setIdJugador(jugador.getId());
        notificacionNueva.setNombreJugador(jugador.getNombre());
        notificacionNueva.setAsunto(nr.getAsunto());
        notificacionNueva.setMensaje(nr.getMensaje());
        notificacionNueva.setEstado("NO_LEIDA");
        notificacionNueva.setFechaCreacion(LocalDateTime.now());

        notificacionRepository.save(notificacionNueva);

        return mapearANotificacionResponse(notificacionNueva);
    }

    @Transactional
    public NotificacionResponse marcarComoLeida(Long id) {
        log.info("Marcando como leída la notificación con ID: {}", id);

        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("La notificación con ID: {} NO EXISTE", id);
                    throw new NotificacionNoExisteException("La notificación con el ID " + id + " no existe");
                });

        if ("LEIDA".equals(notificacion.getEstado())) {
            log.warn("La notificación con ID: {} ya se encuentra leída", id);
            throw new NotificacionYaLeidaException("Esta notificación ya está marcada como leída");
        }

        notificacion.setEstado("LEIDA");

        notificacionRepository.save(notificacion);

        return mapearANotificacionResponse(notificacion);
    }

    public NotificacionResponse obtenerPorId(Long id) {
        log.info("Buscando notificación con ID: {}", id);

        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("La notificación con ID: {} NO EXISTE", id);
                    throw new NotificacionNoExisteException("La notificación con el ID " + id + " no existe");
                });

        return mapearANotificacionResponse(notificacion);
    }

    public List<NotificacionResponse> listarTodas() {
        log.info("Listando todas las notificaciones");

        return notificacionRepository.findAll().stream()
                .map(this::mapearANotificacionResponse)
                .toList();
    }

    public List<NotificacionResponse> listarPorJugador(Long idJugador) {
        log.info("Listando notificaciones para el jugador ID: {}", idJugador);

        return notificacionRepository.findByIdJugador(idJugador).stream()
                .map(this::mapearANotificacionResponse)
                .toList();
    }

    private NotificacionResponse mapearANotificacionResponse(Notificacion n) {
        return NotificacionResponse.builder()
                .id(n.getId())
                .idJugador(n.getIdJugador())
                .nombreJugador(n.getNombreJugador())
                .asunto(n.getAsunto())
                .mensaje(n.getMensaje())
                .estado(n.getEstado())
                .fechaCreacion(n.getFechaCreacion())
                .build();
    }
}