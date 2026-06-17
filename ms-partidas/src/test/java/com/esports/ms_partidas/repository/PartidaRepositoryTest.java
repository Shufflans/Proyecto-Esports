package com.esports.ms_partidas.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.esports.ms_partidas.model.Partida;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PartidaRepositoryTest {

    @Autowired
    private PartidaRepository partidaRepository;

    @Test
    void debeBuscarPorId() {
        Partida partida = Partida.builder()
                .idTorneo(1L)
                .nombreTorneo("Torneo de Verano")
                .idEquipoLocal(10L)
                .idEquipoVisitante(20L)
                .fechaHora(LocalDateTime.now())
                .estado("PROGRAMADA")
                .build();
        Partida guardada = partidaRepository.save(partida);

        Optional<Partida> resultado = partidaRepository.findById(guardada.getId());

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdTorneo());
        assertEquals("PROGRAMADA", resultado.get().getEstado());
    }

    @Test
    void debeRetornarListaPorTorneo() {
        Partida partida = Partida.builder()
                .idTorneo(5L)
                .nombreTorneo("Torneo A")
                .idEquipoLocal(10L)
                .idEquipoVisitante(20L)
                .fechaHora(LocalDateTime.now())
                .estado("PROGRAMADA")
                .build();

        Partida partida2 = Partida.builder()
                .idTorneo(5L)
                .nombreTorneo("Torneo A")
                .idEquipoLocal(30L)
                .idEquipoVisitante(40L)
                .fechaHora(LocalDateTime.now())
                .estado("TERMINADA")
                .build();

        partidaRepository.save(partida);
        partidaRepository.save(partida2);

        List<Partida> resultado = partidaRepository.findByIdTorneo(5L);

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeRetornarListaPorEstado() {
        Partida partida = Partida.builder()
                .idTorneo(1L)
                .idEquipoLocal(10L)
                .idEquipoVisitante(20L)
                .fechaHora(LocalDateTime.now())
                .estado("EN_JUEGO")
                .build();

        Partida partida2 = Partida.builder()
                .idTorneo(2L)
                .idEquipoLocal(30L)
                .idEquipoVisitante(40L)
                .fechaHora(LocalDateTime.now())
                .estado("EN_JUEGO")
                .build();

        partidaRepository.save(partida);
        partidaRepository.save(partida2);

        List<Partida> resultado = partidaRepository.findByEstado("EN_JUEGO");

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeRetornarListaPorEquipoLocalOVisitante() {
        Partida partida = Partida.builder()
                .idTorneo(1L)
                .idEquipoLocal(15L)
                .idEquipoVisitante(20L)
                .fechaHora(LocalDateTime.now())
                .estado("PROGRAMADA")
                .build();

        Partida partida2 = Partida.builder()
                .idTorneo(1L)
                .idEquipoLocal(30L)
                .idEquipoVisitante(15L)
                .fechaHora(LocalDateTime.now())
                .estado("PROGRAMADA")
                .build();

        partidaRepository.save(partida);
        partidaRepository.save(partida2);

        List<Partida> resultado = partidaRepository.findByIdEquipoLocalOrIdEquipoVisitante(15L, 15L);

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeGuardarPartida() {
        Partida partida = Partida.builder()
                .idTorneo(10L)
                .nombreTorneo("Torneo Mundial")
                .idEquipoLocal(10L)
                .idEquipoVisitante(20L)
                .fechaHora(LocalDateTime.now())
                .estado("PROGRAMADA")
                .build();

        Partida resultado = partidaRepository.save(partida);

        assertEquals("Torneo Mundial", resultado.getNombreTorneo());
        assertNotNull(resultado.getId());
    }

    @Test
    void debeListarTodasLasPartidas() {
        Partida partida = Partida.builder()
                .idTorneo(1L)
                .idEquipoLocal(10L)
                .idEquipoVisitante(20L)
                .fechaHora(LocalDateTime.now())
                .estado("PROGRAMADA")
                .build();

        Partida partida2 = Partida.builder()
                .idTorneo(2L)
                .idEquipoLocal(30L)
                .idEquipoVisitante(40L)
                .fechaHora(LocalDateTime.now())
                .estado("TERMINADA")
                .build();

        partidaRepository.save(partida);
        partidaRepository.save(partida2);

        List<Partida> resultado = partidaRepository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarUnaPartida() {
        Partida partida = Partida.builder()
                .idTorneo(1L)
                .idEquipoLocal(10L)
                .idEquipoVisitante(20L)
                .fechaHora(LocalDateTime.now())
                .estado("PROGRAMADA")
                .build();

        Partida guardada = partidaRepository.save(partida);
        partidaRepository.delete(guardada);

        Optional<Partida> resultado = partidaRepository.findById(guardada.getId());

        assertFalse(resultado.isPresent());
    }
}