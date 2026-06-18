package com.esports.ms_rankings.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository;

    @Mock
    private JugadorClient jugadorClient;

    @Mock
    private EquipoClient equipoClient;

    @InjectMocks
    private RankingService rankingService;

    @Test
    void deberiaRegistrarNuevoRanking() {
        RankingRequest peticion = RankingRequest.builder()
                .idJugador(1L)
                .idEquipo(10L)
                .puntos(1500)
                .build();

        JugadorResponse jugador = JugadorResponse.builder().id(1L).nickname("Faker").build();
        EquipoResponse equipo = EquipoResponse.builder().id(10L).nombre("T1").build();

        when(jugadorClient.obtenerJugador(1L, "token")).thenReturn(jugador);
        when(equipoClient.obtenerEquipo(10L, "token")).thenReturn(equipo);
        when(rankingRepository.findByIdJugador(1L)).thenReturn(Collections.emptyList());
        when(rankingRepository.save(any(Ranking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RankingResponse resultado = rankingService.registrarRanking(peticion, "token");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdJugador());
        assertEquals("Faker", resultado.getNombreJugador());
        assertEquals(10L, resultado.getIdEquipo());
        assertEquals("T1", resultado.getNombreEquipo());
        assertEquals(1500, resultado.getPuntos());

        verify(jugadorClient).obtenerJugador(1L, "token");
        verify(equipoClient).obtenerEquipo(10L, "token");
        verify(rankingRepository).findByIdJugador(1L);
        verify(rankingRepository).save(any(Ranking.class));
    }

    @Test
    void deberiaActualizarRankingExistente() {
        RankingRequest peticion = RankingRequest.builder()
                .idJugador(2L)
                .idEquipo(20L)
                .puntos(2000)
                .build();

        JugadorResponse jugador = JugadorResponse.builder().id(2L).nickname("Caps").build();
        EquipoResponse equipo = EquipoResponse.builder().id(20L).nombre("G2 Esports").build();

        Ranking rankingExistente = new Ranking();
        rankingExistente.setId(5L);
        rankingExistente.setIdJugador(2L);
        rankingExistente.setPuntos(1000);

        when(jugadorClient.obtenerJugador(2L, "token")).thenReturn(jugador);
        when(equipoClient.obtenerEquipo(20L, "token")).thenReturn(equipo);
        when(rankingRepository.findByIdJugador(2L)).thenReturn(List.of(rankingExistente));
        when(rankingRepository.save(any(Ranking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RankingResponse resultado = rankingService.registrarRanking(peticion, "token");

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals(2000, resultado.getPuntos());

        verify(rankingRepository).save(any(Ranking.class));
    }

    @Test
    void deberiaRetornarTodosLosRankings() {
        Ranking ranking = new Ranking();
        ranking.setId(1L);
        ranking.setNombreJugador("Chovy");

        when(rankingRepository.findAll()).thenReturn(List.of(ranking));

        List<RankingResponse> resultado = rankingService.listarTodosLosRankings();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Chovy", resultado.get(0).getNombreJugador());
        verify(rankingRepository).findAll();
    }

    @Test
    void deberiaRetornarRankingsPorJugador() {
        Ranking ranking = new Ranking();
        ranking.setIdJugador(5L);
        ranking.setNombreJugador("Rookie");

        when(rankingRepository.findByIdJugador(5L)).thenReturn(List.of(ranking));

        List<RankingResponse> resultado = rankingService.listarRankingsPorJugador(5L);

        assertFalse(resultado.isEmpty());
        assertEquals("Rookie", resultado.get(0).getNombreJugador());
        verify(rankingRepository).findByIdJugador(5L);
    }

    @Test
    void deberiaRetornarRankingsPorEquipo() {
        Ranking ranking = new Ranking();
        ranking.setIdEquipo(15L);
        ranking.setNombreEquipo("Fnatic");

        when(rankingRepository.findByIdEquipo(15L)).thenReturn(List.of(ranking));

        List<RankingResponse> resultado = rankingService.listarRankingsPorEquipo(15L);

        assertFalse(resultado.isEmpty());
        assertEquals("Fnatic", resultado.get(0).getNombreEquipo());
        verify(rankingRepository).findByIdEquipo(15L);
    }

    @Test
    void deberiaEliminarRanking() {
        Ranking ranking = new Ranking();
        ranking.setId(1L);

        when(rankingRepository.findById(1L)).thenReturn(Optional.of(ranking));

        rankingService.eliminarRanking(1L);

        verify(rankingRepository).findById(1L);
        verify(rankingRepository).delete(ranking);
    }

    // Tests ERRORES

    @Test
    void deberiaLanzarJugadorNoEncontradoAlRegistrar() {
        RankingRequest peticion = RankingRequest.builder().idJugador(99L).build();

        when(jugadorClient.obtenerJugador(99L, "token")).thenReturn(null);

        JugadorNoEncontradoException ex = assertThrows(JugadorNoEncontradoException.class,
                () -> rankingService.registrarRanking(peticion, "token"));

        assertNotNull(ex);
        assertEquals("El jugador con la ID: 99 no existe", ex.getMessage());
        verify(rankingRepository, never()).save(any(Ranking.class));
    }

    @Test
    void deberiaLanzarEquipoNoEncontradoAlRegistrar() {
        RankingRequest peticion = RankingRequest.builder()
                .idJugador(1L)
                .idEquipo(99L)
                .build();

        when(jugadorClient.obtenerJugador(1L, "token")).thenReturn(JugadorResponse.builder().id(1L).build());
        when(equipoClient.obtenerEquipo(99L, "token")).thenReturn(null);

        EquipoNoEncontradoException ex = assertThrows(EquipoNoEncontradoException.class,
                () -> rankingService.registrarRanking(peticion, "token"));

        assertNotNull(ex);
        assertEquals("El equipo con la ID: 99 no existe", ex.getMessage());
        verify(rankingRepository, never()).save(any(Ranking.class));
    }

    @Test
    void deberiaLanzarRankingNoEncontradoAlListarPorJugadorVacio() {
        when(rankingRepository.findByIdJugador(99L)).thenReturn(Collections.emptyList());

        RankingNoEncontradoException ex = assertThrows(RankingNoEncontradoException.class,
                () -> rankingService.listarRankingsPorJugador(99L));

        assertNotNull(ex);
        assertEquals("No hay rankings registrados para el jugador con ID: 99", ex.getMessage());
    }

    @Test
    void deberiaLanzarRankingNoEncontradoAlListarPorEquipoVacio() {
        when(rankingRepository.findByIdEquipo(99L)).thenReturn(Collections.emptyList());

        RankingNoEncontradoException ex = assertThrows(RankingNoEncontradoException.class,
                () -> rankingService.listarRankingsPorEquipo(99L));

        assertNotNull(ex);
        assertEquals("No hay rankings registrados para el equipo con ID: 99", ex.getMessage());
    }

    @Test
    void deberiaLanzarRankingNoEncontradoAlEliminar() {
        when(rankingRepository.findById(99L)).thenReturn(Optional.empty());

        RankingNoEncontradoException ex = assertThrows(RankingNoEncontradoException.class,
                () -> rankingService.eliminarRanking(99L));

        assertNotNull(ex);
        assertEquals("Ranking con la ID: 99 no encontrado.", ex.getMessage());
        verify(rankingRepository, never()).delete(any(Ranking.class));
    }
}