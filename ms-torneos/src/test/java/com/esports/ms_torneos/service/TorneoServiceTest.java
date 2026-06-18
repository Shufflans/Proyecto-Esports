package com.esports.ms_torneos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class TorneoServiceTest {

    @Mock
    private TorneoRepository torneoRepository;

    @Mock
    private JuegoClient juegoClient;

    @InjectMocks
    private TorneoService torneoService;

    @Test
    void deberiaCrearUnTorneo() {
        TorneoRequest peticion = TorneoRequest.builder()
                .idJuego(1L)
                .nombreTorneo("VCT Masters 2026")
                .organizador("Riot Games")
                .fechaInicio(LocalDate.of(2026, 8, 15))
                .fechaFin(LocalDate.of(2026, 8, 30))
                .totalPremio(50000.0)
                .maxEquipos(16)
                .build();

        JuegoResponse juegoFalso = JuegoResponse.builder()
                .nombreJuego("Valorant")
                .activo(true)
                .build();

        when(juegoClient.obtenerJuego(1L, "Bearer Token")).thenReturn(juegoFalso);
        when(torneoRepository.save(any(Torneo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TorneoResponse resultado = torneoService.crearTorneo(peticion, "Bearer Token");

        assertNotNull(resultado);
        assertEquals("VCT Masters 2026", resultado.getNombreTorneo());
        assertEquals("Valorant", resultado.getNombreJuego());
        assertEquals("PROGRAMADO", resultado.getEstado());
        verify(juegoClient).obtenerJuego(1L, "Bearer Token");
        verify(torneoRepository).save(any(Torneo.class));
    }

    @Test
    void deberiaRetornarUnTorneoPorSuId() {
        Torneo torneo = Torneo.builder()
                .id(1L)
                .nombreTorneo("VCT Masters 2026")
                .estado("PROGRAMADO")
                .build();

        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));

        TorneoResponse resultado = torneoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("VCT Masters 2026", resultado.getNombreTorneo());
        verify(torneoRepository).findById(1L);
    }

    @Test
    void deberiaRetornarListaDeTorneos() {
        Torneo torneo = Torneo.builder()
                .id(1L)
                .nombreTorneo("VCT Masters 2026")
                .build();

        when(torneoRepository.findAll()).thenReturn(List.of(torneo));

        List<TorneoResponse> resultado = torneoService.listarTodosLosTorneos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("VCT Masters 2026", resultado.get(0).getNombreTorneo());
        verify(torneoRepository).findAll();
    }

    @Test
    void deberiaRetornarListaDeTorneosPorEstado() {
        Torneo torneo = Torneo.builder()
                .id(1L)
                .nombreTorneo("VCT Masters 2026")
                .estado("PROGRAMADO")
                .build();

        when(torneoRepository.findByEstado("PROGRAMADO")).thenReturn(List.of(torneo));

        List<TorneoResponse> resultado = torneoService.listarTorneosPorEstado("PROGRAMADO");

        assertFalse(resultado.isEmpty());
        assertEquals("PROGRAMADO", resultado.get(0).getEstado());
        verify(torneoRepository).findByEstado("PROGRAMADO");
    }

    @Test
    void deberiaRetornarListaDeTorneosPorIdJuego() {
        Torneo torneo = Torneo.builder()
                .id(1L)
                .idJuego(5L)
                .nombreTorneo("Torneo LoL")
                .build();

        when(torneoRepository.findByIdJuego(5L)).thenReturn(List.of(torneo));

        List<TorneoResponse> resultado = torneoService.listarTorneosPorIdJuego(5L);

        assertFalse(resultado.isEmpty());
        assertEquals(5L, resultado.get(0).getIdJuego());
        verify(torneoRepository).findByIdJuego(5L);
    }

    @Test
    void deberiaActualizarTorneo() {
        Torneo torneoGuardado = Torneo.builder()
                .id(1L)
                .nombreTorneo("VCT Masters")
                .totalPremio(50000.0)
                .estado("PROGRAMADO")
                .build();

        TorneoRequest peticionActualizar = TorneoRequest.builder()
                .nombreTorneo("VCT Masters Premium")
                .totalPremio(100000.0)
                .build();

        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneoGuardado));
        when(torneoRepository.save(any(Torneo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TorneoResponse resultado = torneoService.actualizarTorneo(1L, peticionActualizar);

        assertNotNull(resultado);
        assertEquals("VCT Masters Premium", resultado.getNombreTorneo());
        assertEquals(100000.0, resultado.getTotalPremio());
        verify(torneoRepository).findById(1L);
        verify(torneoRepository).save(any(Torneo.class));
    }

    @Test
    void deberiaDesactivarAlTorneoPorId() {
        Torneo torneoGuardado = Torneo.builder()
                .id(1L)
                .estado("PROGRAMADO")
                .build();

        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneoGuardado));

        torneoService.desactivarTorneo(1L);

        assertEquals("TERMINADO", torneoGuardado.getEstado());
        verify(torneoRepository).save(torneoGuardado);
    }

    // Tests ERRORES

    @Test
    void deberiaLanzarErrorPorJuegoNoEncontradoAlCrear() {
        TorneoRequest peticion = TorneoRequest.builder()
                .idJuego(99L)
                .build();

        when(juegoClient.obtenerJuego(99L, "Token")).thenReturn(null);

        NoExisteJuegoConIdException ex = assertThrows(NoExisteJuegoConIdException.class,
                () -> torneoService.crearTorneo(peticion, "Token"));

        assertNotNull(ex);
        assertEquals("El juego con la ID: 99 no existe", ex.getMessage());
        verify(torneoRepository, never()).save(any(Torneo.class));
    }

    @Test
    void deberiaLanzarErrorPorJuegoDesactivadoAlCrear() {
        TorneoRequest peticion = TorneoRequest.builder()
                .idJuego(1L)
                .build();

        JuegoResponse juegoDesactivado = JuegoResponse.builder()
                .activo(false)
                .build();

        when(juegoClient.obtenerJuego(1L, "Token")).thenReturn(juegoDesactivado);

        JuegoDesactivadoException ex = assertThrows(JuegoDesactivadoException.class,
                () -> torneoService.crearTorneo(peticion, "Token"));

        assertNotNull(ex);
        assertEquals("El Juego con la ID: 1 está desactivado", ex.getMessage());
        verify(torneoRepository, never()).save(any(Torneo.class));
    }

    @Test
    void deberiaLanzarErrorPorTorneoNoEncontrado() {
        when(torneoRepository.findById(99L)).thenReturn(Optional.empty());

        TorneoNoEncontradoException ex = assertThrows(TorneoNoEncontradoException.class,
                () -> torneoService.buscarPorId(99L));

        assertNotNull(ex);
        assertEquals("Torneo con la ID: 99 no encontrado.", ex.getMessage());
        verify(torneoRepository).findById(99L);
    }

    @Test
    void deberiaLanzarTorneoNoEncontradoAlActualizar() {
        TorneoRequest peticion = TorneoRequest.builder().build();

        when(torneoRepository.findById(99L)).thenReturn(Optional.empty());

        TorneoNoEncontradoException ex = assertThrows(TorneoNoEncontradoException.class,
                () -> torneoService.actualizarTorneo(99L, peticion));

        assertNotNull(ex);
        assertEquals("Torneo con la ID: 99 no encontrado.", ex.getMessage());
        verify(torneoRepository).findById(99L);
        verify(torneoRepository, never()).save(any(Torneo.class));
    }

    @Test
    void deberiaLanzarTorneoFinalizadoAlActualizar() {
        Torneo torneoFinalizado = Torneo.builder()
                .id(1L)
                .estado("FINALIZADO")
                .build();

        TorneoRequest peticion = TorneoRequest.builder().build();

        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneoFinalizado));

        TorneoFinalizadoException ex = assertThrows(TorneoFinalizadoException.class,
                () -> torneoService.actualizarTorneo(1L, peticion));

        assertNotNull(ex);
        assertEquals("No puedes modificar un torneo ya finalizado", ex.getMessage());
        verify(torneoRepository, never()).save(any(Torneo.class));
    }

    @Test
    void deberiaLanzarTorneoNoEncontradoAlDesactivar() {
        when(torneoRepository.findById(99L)).thenReturn(Optional.empty());

        TorneoNoEncontradoException ex = assertThrows(TorneoNoEncontradoException.class,
                () -> torneoService.desactivarTorneo(99L));

        assertNotNull(ex);
        assertEquals("Torneo con la ID: 99 no encontrado.", ex.getMessage());
        verify(torneoRepository).findById(99L);
        verify(torneoRepository, never()).save(any(Torneo.class));
    }

}