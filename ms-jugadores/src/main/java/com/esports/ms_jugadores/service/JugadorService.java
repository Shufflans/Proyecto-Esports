package com.esports.ms_jugadores.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esports.ms_jugadores.dto.JugadorRequest;
import com.esports.ms_jugadores.dto.JugadorResponse;
import com.esports.ms_jugadores.exception.EdadMinimaException;
import com.esports.ms_jugadores.exception.JugadorNoEncontradoException;
import com.esports.ms_jugadores.exception.NickNameDuplicadoException;
import com.esports.ms_jugadores.model.Jugador;
import com.esports.ms_jugadores.repository.JugadorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private static final Integer edadMinima = 16;

    @Transactional
    public JugadorResponse crear(JugadorRequest jr) {
        log.info("Creando jugador: {}", jr.getNickname());

        if (jugadorRepository.existsByNickname(jr.getNickname())) {
            log.warn("El nickname del jugador ya existe.");

            throw new NickNameDuplicadoException(
                    "No se pudo registrar, el nickname " + jr.getNickname() + "Ya existe.");
        }

        Integer edad = Period.between(jr.getFechaNacimiento(), LocalDate.now()).getYears();

        if (edad < edadMinima) {
            log.warn("La edad mínima es 16 años.");
            throw new EdadMinimaException("La edad minima debe ser igual o mayor a 16");
        }

        Jugador jugador = Jugador.builder()
                .nickname(jr.getNickname())
                .nombreReal(jr.getNombreReal())
                .pais(jr.getPais())
                .fechaNacimiento(jr.getFechaNacimiento())
                .rol(jr.getRol())
                .idEquipoActual(jr.getIdEquipoActual())
                .activo(true)
                .salarioMensual(jr.getSalarioMensual())
                .build();

        Jugador jugadorGuardado = jugadorRepository.save(jugador);

        return mapearAResponse(jugadorGuardado);
    }

    @Transactional
    public JugadorResponse buscarPorId(Long id) {
        log.info("Buscando jugador por ID: {} ", id);

        Jugador jugadorEncontrado = jugadorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Jugador no encontrado, ID: {}", id);
                    return new JugadorNoEncontradoException("Jugador con la id " + id + "No encontrado");
                });

        return mapearAResponse(jugadorEncontrado);
    }

    @Transactional
    public List<JugadorResponse> mostrarPorPaises(String pais) {
        log.info("Buscando todos los jugadores por el pais: {}", pais);
        return jugadorRepository.findByPais(pais).stream()
                .map(this::mapearAResponse)
                .toList();
    }

    @Transactional
    public List<JugadorResponse> listarTodos() {
        log.info("Listando todos los jugadores registrados");
        List<JugadorResponse> lista = jugadorRepository.findAll().stream()
                .map(this::mapearAResponse)
                .toList();
        log.info("Total de Jugadores listados: {}", lista.size());
        return lista;
    }

    @Transactional
    public JugadorResponse actualizar(Long id, JugadorRequest jr) {
        Jugador jugadorEncontrado = jugadorRepository.findById(id).orElseThrow(() -> {
            log.warn("No se han encontrado jugadores con la ID: {}", id);
            return new JugadorNoEncontradoException("Jugador con la ID " + id + "No encontrado");
        });

        if (!jugadorEncontrado.getNickname().equals(jr.getNickname())
                && jugadorRepository.existsByNickname(jr.getNickname())) {
            log.warn("El nuevo nickname '{}' ya está siendo ocupado por otro jugador", jr.getNickname());
            throw new NickNameDuplicadoException("El nickname: " + jr.getNickname() + " Ya existe");
        }

        jugadorEncontrado.setNickname(jr.getNickname());
        jugadorEncontrado.setNombreReal(jr.getNombreReal());
        jugadorEncontrado.setPais(jr.getPais());
        jugadorEncontrado.setFechaNacimiento(jr.getFechaNacimiento());
        jugadorEncontrado.setRol(jr.getRol());
        jugadorEncontrado.setIdEquipoActual(jr.getIdEquipoActual());
        jugadorEncontrado.setSalarioMensual(jr.getSalarioMensual());

        Jugador jugadorEncontradoGuardado = jugadorRepository.save(jugadorEncontrado);

        return mapearAResponse(jugadorEncontradoGuardado);
    }

    @Transactional
    public void desactivarJugador(Long id) {
        log.info("El jugador con ID: '{}' ha sido eliminado", id);

        Jugador jugadorEncontrado = jugadorRepository.findById(id).orElseThrow(() -> {
            log.warn("Jugador con ID: '{}' no ha sido encontrado", id);
            return new JugadorNoEncontradoException("Jugador con ID: " + id + " No encontrado");
        });

        jugadorEncontrado.setActivo(false);
        jugadorRepository.save(jugadorEncontrado);

    }

    private JugadorResponse mapearAResponse(Jugador jugador) {
        return JugadorResponse.builder()
                .id(jugador.getId())
                .nickname(jugador.getNickname())
                .nombreReal(jugador.getNombreReal())
                .pais(jugador.getPais())
                .fechaNacimiento(jugador.getFechaNacimiento())
                .rol(jugador.getRol())
                .idEquipoActual(jugador.getIdEquipoActual())
                .activo(jugador.getActivo())
                .salarioMensual(jugador.getSalarioMensual())
                .build();
    }
}
