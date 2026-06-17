package com.esports.ms_partidas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private TorneoClient torneoClient;

    @Mock
    private EquipoClient equipoClient;

    @InjectMocks
    private PartidaService partidaService;

    @Test
    void deberiaCrearUnaPartida() {
        PartidaRequest peticion = PartidaRequest.builder()
                .idTorneo(1L)
                .idEquipoLocal(10L)
                .idEquipoVisitante(20L)
                .fechaHora(LocalDateTime.now())
                .build();

        TorneoResponse torneo = TorneoResponse.builder().id(1L).nombre("Torneo de Verano").build();
        EquipoResponse equipoLocal = EquipoResponse.builder().id(10L).nombre("Equipo A").activo(true).build();
        EquipoResponse equipoVisita = EquipoResponse.builder().id(20L).nombre("Equipo B").activo(true).build();

        when(torneoClient.obtenerTorneo(1L, "token")).thenReturn(torneo);
        when(equipoClient.obtenerEquipo(10L, "token")).thenReturn(equipoLocal);
        when(equipoClient.obtenerEquipo(20L, "token")).thenReturn(equipoVisita);
        when(partidaRepository.save(any(Partida.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PartidaResponse resultado = partidaService.crearPartida(peticion, "token");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdTorneo());
        assertEquals("Torneo de Verano", resultado.getNombreTorneo());
        assertEquals("Equipo A", resultado.getNombreEquipoLocal());
        assertEquals("Equipo B", resultado.getNombreEquipoVisitante());
        assertEquals("PROGRAMADA", resultado.getEstado());

        verify(torneoClient).obtenerTorneo(1L, "token");
        verify(equipoClient).obtenerEquipo(10L, "token");
        verify(equipoClient).obtenerEquipo(20L, "token");
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void deberiaFinalizarPartida() {
        Partida partidaGuardada = Partida.builder()
                .id(1L)
                .idEquipoLocal(10L)
                .idEquipoVisitante(20L)
                .estado("PROGRAMADA")
                .build();

        ResultadoPartidaRequest peticionFinalizar = ResultadoPartidaRequest.builder()
                .marcadorLocal(3)
                .marcadorVisitante(1)
                .duracionMinutos(45)
                .idEquipoGanador(10L)
                .build();

        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partidaGuardada));
        when(partidaRepository.save(any(Partida.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PartidaResponse resultado = partidaService.finalizarPartida(1L, peticionFinalizar);

        assertNotNull(resultado);
        assertEquals(3, resultado.getMarcadorLocal());
        assertEquals(1, resultado.getMarcadorVisitante());
        assertEquals(45, resultado.getDuracionMinutos());
        assertEquals(10L, resultado.getIdEquipoGanador());
        assertEquals("TERMINADA", resultado.getEstado());

        verify(partidaRepository).findById(1L);
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void deberiaRetornarUnaPartidaPorSuId() {
        Partida partida = Partida.builder()
                .id(1L)
                .nombreTorneo("Torneo Masters")
                .build();

        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        PartidaResponse resultado = partidaService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Torneo Masters", resultado.getNombreTorneo());
        verify(partidaRepository).findById(1L);
    }

    @Test
    void deberiaRetornarListaDePartidas() {
        Partida partida = Partida.builder()
                .id(1L)
                .nombreTorneo("Liga Pro")
                .build();

        when(partidaRepository.findAll()).thenReturn(List.of(partida));

        List<PartidaResponse> resultado = partidaService.listarTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Liga Pro", resultado.get(0).getNombreTorneo());
        verify(partidaRepository).findAll();
    }

    // Tests ERRORES
    @Test
    void deberiaLanzarTorneoNoExisteAlCrear() {
        PartidaRequest peticion = PartidaRequest.builder().idTorneo(99L).idEquipoLocal(10L).idEquipoVisitante(20L)
                .build();

        when(torneoClient.obtenerTorneo(99L, "token")).thenReturn(null);
        when(equipoClient.obtenerEquipo(10L, "token")).thenReturn(EquipoResponse.builder().id(10L).build());
        when(equipoClient.obtenerEquipo(20L, "token")).thenReturn(EquipoResponse.builder().id(20L).build());

        TorneoNoExisteException ex = assertThrows(TorneoNoExisteException.class,
                () -> partidaService.crearPartida(peticion, "token"));

        assertNotNull(ex);
        assertEquals("El torneo con el ID 99 no existe", ex.getMessage());
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deberiaLanzarEquiposNoExistenAlCrear() {
        PartidaRequest peticion = PartidaRequest.builder().idTorneo(1L).idEquipoLocal(99L).idEquipoVisitante(20L)
                .build();

        TorneoResponse torneo = TorneoResponse.builder().id(1L).build();

        when(torneoClient.obtenerTorneo(1L, "token")).thenReturn(torneo);
        when(equipoClient.obtenerEquipo(99L, "token")).thenReturn(null);
        when(equipoClient.obtenerEquipo(20L, "token")).thenReturn(EquipoResponse.builder().id(20L).build());

        EquiposNoExistenException ex = assertThrows(EquiposNoExistenException.class,
                () -> partidaService.crearPartida(peticion, "token"));

        assertNotNull(ex);
        assertEquals("El Equipo Local o Visitante no existen", ex.getMessage());
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deberiaLanzarMismoEquipoAlCrear() {
        PartidaRequest peticion = PartidaRequest.builder().idTorneo(1L).idEquipoLocal(10L).idEquipoVisitante(10L)
                .build();

        TorneoResponse torneo = TorneoResponse.builder().id(1L).build();
        EquipoResponse equipoLocal = EquipoResponse.builder().id(10L).build();
        EquipoResponse equipoVisita = EquipoResponse.builder().id(10L).build(); // Mismo ID

        when(torneoClient.obtenerTorneo(1L, "token")).thenReturn(torneo);
        when(equipoClient.obtenerEquipo(10L, "token")).thenReturn(equipoLocal);
        when(equipoClient.obtenerEquipo(10L, "token")).thenReturn(equipoVisita);

        MismoEquipoException ex = assertThrows(MismoEquipoException.class,
                () -> partidaService.crearPartida(peticion, "token"));

        assertNotNull(ex);
        assertEquals("El equipo visitante y local no pueden ser el mismo", ex.getMessage());
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deberiaLanzarEquiposDesactivadosAlCrear() {
        PartidaRequest peticion = PartidaRequest.builder().idTorneo(1L).idEquipoLocal(10L).idEquipoVisitante(20L)
                .build();

        TorneoResponse torneo = TorneoResponse.builder().id(1L).build();
        EquipoResponse equipoLocal = EquipoResponse.builder().id(10L).activo(true).build();
        EquipoResponse equipoVisita = EquipoResponse.builder().id(20L).activo(false).build(); // Desactivado

        when(torneoClient.obtenerTorneo(1L, "token")).thenReturn(torneo);
        when(equipoClient.obtenerEquipo(10L, "token")).thenReturn(equipoLocal);
        when(equipoClient.obtenerEquipo(20L, "token")).thenReturn(equipoVisita);

        EquiposDesactivadosException ex = assertThrows(EquiposDesactivadosException.class,
                () -> partidaService.crearPartida(peticion, "token"));

        assertNotNull(ex);
        assertEquals("El Equipo Local o Visitante están desactivados", ex.getMessage());
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deberiaLanzarPartidaNoExisteAlFinalizar() {
        ResultadoPartidaRequest peticionFinalizar = ResultadoPartidaRequest.builder().build();

        when(partidaRepository.findById(99L)).thenReturn(Optional.empty());

        PartidaNoExisteException ex = assertThrows(PartidaNoExisteException.class,
                () -> partidaService.finalizarPartida(99L, peticionFinalizar));

        assertNotNull(ex);
        assertEquals("La partida con el ID 99 no existe", ex.getMessage());
        verify(partidaRepository).findById(99L);
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deberiaLanzarPartidaYaFinalizadaAlFinalizar() {
        Partida partidaGuardada = Partida.builder()
                .id(1L)
                .estado("TERMINADA") // Ya terminada
                .build();

        ResultadoPartidaRequest peticionFinalizar = ResultadoPartidaRequest.builder().build();

        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partidaGuardada));

        PartidaYaFinalizadaException ex = assertThrows(PartidaYaFinalizadaException.class,
                () -> partidaService.finalizarPartida(1L, peticionFinalizar));

        assertNotNull(ex);
        assertEquals("Esta partida ya esta terminada y no puede ser modificada", ex.getMessage());
        verify(partidaRepository).findById(1L);
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deberiaLanzarGanadorInvalidoAlFinalizar() {
        Partida partidaGuardada = Partida.builder()
                .id(1L)
                .idEquipoLocal(10L)
                .idEquipoVisitante(20L)
                .estado("PROGRAMADA")
                .build();

        ResultadoPartidaRequest peticionFinalizar = ResultadoPartidaRequest.builder()
                .idEquipoGanador(99L) // Ganador que no jugó
                .build();

        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partidaGuardada));

        GanadorInvalidoException ex = assertThrows(GanadorInvalidoException.class,
                () -> partidaService.finalizarPartida(1L, peticionFinalizar));

        assertNotNull(ex);
        assertEquals("El equipo ganador debe ser el local o el visitante", ex.getMessage());
        verify(partidaRepository).findById(1L);
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void deberiaLanzarPartidaNoExisteAlObtenerPorId() {
        when(partidaRepository.findById(99L)).thenReturn(Optional.empty());

        PartidaNoExisteException ex = assertThrows(PartidaNoExisteException.class,
                () -> partidaService.obtenerPorId(99L));

        assertNotNull(ex);
        assertEquals("La partida con el ID 99 no existe", ex.getMessage());
        verify(partidaRepository).findById(99L);
    }
}