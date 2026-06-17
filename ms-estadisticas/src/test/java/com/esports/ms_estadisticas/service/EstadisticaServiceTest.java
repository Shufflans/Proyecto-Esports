package com.esports.ms_estadisticas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstadisticaServiceTest {

    @Mock
    private EstadisticaRepository estadisticaRepository;

    @Mock
    private JugadorClient jugadorClient;

    @Mock
    private PartidaClient partidaClient;

    @InjectMocks
    private EstadisticaService service;

    @Test
    void deberiaCrearEstadistica() {
        String token = "Token";

        EstadisticaRequest estadisticaPeticion = EstadisticaRequest.builder()
                .idJugador(1L)
                .idPartida(1L)
                .asesinatos(10)
                .muertes(2)
                .asistencias(5)
                .mvp(true)
                .build();

        JugadorResponse jugadorRespuesta = JugadorResponse.builder()
                .id(1L)
                .nickname("Faker")
                .build();

        PartidaResponse partidaRespuesta = PartidaResponse.builder()
                .id(1L)
                .build();

        Estadistica estadisticaGuardada = Estadistica.builder()
                .id(1L)
                .idJugador(1L)
                .nicknameJugador("Faker")
                .idPartida(1L)
                .asesinatos(10)
                .muertes(2)
                .asistencias(5)
                .kda(7.5)
                .mvp(true)
                .build();

        when(estadisticaRepository.existsByIdJugadorAndIdPartida(1L, 1L)).thenReturn(false);
        when(jugadorClient.obtenerJugador(1L, token)).thenReturn(jugadorRespuesta);
        when(partidaClient.obtenerPartida(1L, token)).thenReturn(partidaRespuesta);
        when(estadisticaRepository.save(any(Estadistica.class))).thenReturn(estadisticaGuardada);

        EstadisticaResponse resultado = service.crearEstadistica(estadisticaPeticion, token);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getIdJugador());
        assertEquals("Faker", resultado.getNicknameJugador());
        assertEquals(7.5, resultado.getKda());
        assertTrue(resultado.getMvp());
        verify(estadisticaRepository).save(any(Estadistica.class));
    }

    @Test
    void deberiaRetornarUnaListaDeEstadisticasPorJugador() {
        Estadistica estadisticaGuardada = Estadistica.builder()
                .id(1L)
                .idJugador(1L)
                .idPartida(2L)
                .nicknameJugador("Faker")
                .kda(7.5)
                .build();

        when(estadisticaRepository.findByIdJugador(1L)).thenReturn(List.of(estadisticaGuardada));

        List<EstadisticaResponse> resultado = service.listarEstadisticasPorJugador(1L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(1L, resultado.get(0).getIdJugador());
        assertEquals("Faker", resultado.get(0).getNicknameJugador());
        verify(estadisticaRepository).findByIdJugador(1L);
    }

    @Test
    void deberiaRetornarUnaListaDeEstadisticasPorPartida() {
        Estadistica estadisticaGuardada = Estadistica.builder()
                .id(1L)
                .idJugador(1L)
                .idPartida(2L)
                .nicknameJugador("Faker")
                .build();

        when(estadisticaRepository.findByIdPartida(2L)).thenReturn(List.of(estadisticaGuardada));

        List<EstadisticaResponse> resultado = service.listarEstadisticasPorPartida(2L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(0).getIdPartida());
        verify(estadisticaRepository).findByIdPartida(2L);
    }

    // TEST DE ERRORES

    @Test
    void deberiaLanzarErrorSiEstadisticaYaExiste() {
        EstadisticaRequest estadisticaPeticion = EstadisticaRequest.builder()
                .idJugador(1L)
                .idPartida(1L)
                .build();

        when(estadisticaRepository.existsByIdJugadorAndIdPartida(1L, 1L)).thenReturn(true);

        EstadisticaYaExisteException ex = assertThrows(EstadisticaYaExisteException.class,
                () -> service.crearEstadistica(estadisticaPeticion, "token"));

        assertNotNull(ex);
        assertEquals("El jugador con ID 1 ya tiene estadísticas registradas para la partida 1", ex.getMessage());
        verify(estadisticaRepository, never()).save(any(Estadistica.class));
    }

    @Test
    void deberiaLanzarQueElJugadorNoExiste() {
        EstadisticaRequest estadisticaPeticion = EstadisticaRequest.builder()
                .idJugador(2L)
                .idPartida(1L)
                .build();

        when(estadisticaRepository.existsByIdJugadorAndIdPartida(2L, 1L)).thenReturn(false);
        when(jugadorClient.obtenerJugador(2L, "token")).thenReturn(null);

        JugadorNoEncontradoException ex = assertThrows(JugadorNoEncontradoException.class,
                () -> service.crearEstadistica(estadisticaPeticion, "token"));

        assertNotNull(ex);
        assertEquals("Jugador con la ID: 2 no encontrado", ex.getMessage());
        verify(jugadorClient).obtenerJugador(2L, "token");
        verify(estadisticaRepository, never()).save(any(Estadistica.class));
    }

    @Test
    void deberiaLanzarQueLaPartidaNoExiste() {
        EstadisticaRequest estadisticaPeticion = EstadisticaRequest.builder()
                .idJugador(1L)
                .idPartida(2L)
                .build();

        JugadorResponse jugadorRespuesta = JugadorResponse.builder()
                .id(1L)
                .nickname("Faker")
                .build();

        when(estadisticaRepository.existsByIdJugadorAndIdPartida(1L, 2L)).thenReturn(false);
        when(jugadorClient.obtenerJugador(1L, "token")).thenReturn(jugadorRespuesta);
        when(partidaClient.obtenerPartida(2L, "token")).thenReturn(null);

        PartidaNoEncontradaException ex = assertThrows(PartidaNoEncontradaException.class,
                () -> service.crearEstadistica(estadisticaPeticion, "token"));

        assertNotNull(ex);
        assertEquals("Partida con la ID: 2 no encontrada", ex.getMessage());
        verify(partidaClient).obtenerPartida(2L, "token");
        verify(estadisticaRepository, never()).save(any(Estadistica.class));
    }
}