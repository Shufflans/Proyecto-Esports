package com.esports.ms_juegos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.esports.ms_juegos.dto.JuegoRequest;
import com.esports.ms_juegos.dto.JuegoResponse;
import com.esports.ms_juegos.exception.JuegoNoEncontradoException;
import com.esports.ms_juegos.exception.NombreDeJuegoExisteException;
import com.esports.ms_juegos.model.Juego;
import com.esports.ms_juegos.repository.JuegoRepository;

@ExtendWith(MockitoExtension.class)
public class JuegoServiceTest {

    @Mock
    private JuegoRepository juegoRepository;

    @InjectMocks
    private JuegoService juegoService;

    @Test
    void deberiarCrearUnJuego() {
        JuegoRequest peticion = JuegoRequest.builder()
                .nombreJuego("League of Legends")
                .generoJuego("MOBA")
                .totalPremio(1000.0)
                .build();

        when(juegoRepository.existsByNombreJuego(peticion.getNombreJuego())).thenReturn(false);
        when(juegoRepository.save(any(Juego.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JuegoResponse resultado = juegoService.crearJuego(peticion);

        assertNotNull(resultado);
        assertEquals("League of Legends", resultado.getNombreJuego());
        assertEquals("MOBA", resultado.getGeneroJuego());
        assertEquals(1000.0, resultado.getTotalPremio());
        assertEquals(true, resultado.getActivo());
        verify(juegoRepository).existsByNombreJuego("League of Legends");
        verify(juegoRepository).save(any(Juego.class));
    }

    @Test
    void deberiaRetornarUnJuegoPorSuId() {
        Juego juego = Juego.builder()
                .id(1L)
                .nombreJuego("League of Legends")
                .build();

        when(juegoRepository.findById(1L)).thenReturn(Optional.of(juego));

        JuegoResponse resultado = juegoService.buscarJuegoPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("League of Legends", resultado.getNombreJuego());
        verify(juegoRepository).findById(1L);
    }

    @Test
    void deberiaRetornarListaDeJuegos() {
        Juego juego = Juego.builder()
                .id(1L)
                .nombreJuego("Valorant")
                .build();

        when(juegoRepository.findAll()).thenReturn(List.of(juego));

        List<JuegoResponse> resultado = juegoService.listarTodosLosJuegos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Valorant", resultado.get(0).getNombreJuego());
        verify(juegoRepository).findAll();
    }

    @Test
    void deberiaRetornarListaDeJuegosActivos() {
        Juego juego = Juego.builder()
                .id(1L)
                .nombreJuego("Counter Strike 2")
                .activo(true)
                .build();

        when(juegoRepository.findByActivoTrue()).thenReturn(List.of(juego));

        List<JuegoResponse> resultado = juegoService.listarJuegosActivos();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.get(0).getActivo());
        verify(juegoRepository).findByActivoTrue();
    }

    @Test
    void deberiaRetornarListaDeJuegosPorGenero() {
        Juego juego = Juego.builder()
                .id(1L)
                .nombreJuego("Dota 2")
                .generoJuego("MOBA")
                .build();

        when(juegoRepository.findByGeneroJuego("MOBA")).thenReturn(List.of(juego));

        List<JuegoResponse> resultado = juegoService.buscarJuegoPorGenero("MOBA");

        assertFalse(resultado.isEmpty());
        assertEquals("MOBA", resultado.get(0).getGeneroJuego());
        verify(juegoRepository).findByGeneroJuego("MOBA");
    }

    @Test
    void deberiaActualizarJuego() {
        Juego juegoGuardado = Juego.builder()
                .id(1L)
                .nombreJuego("FIFA 23")
                .generoJuego("Deportes")
                .totalPremio(10000.0)
                .activo(true)
                .build();

        JuegoRequest peticionActualizar = JuegoRequest.builder()
                .nombreJuego("EA FC 24")
                .generoJuego("Deportes")
                .totalPremio(20000.0)
                .build();

        when(juegoRepository.findById(1L)).thenReturn(Optional.of(juegoGuardado));
        when(juegoRepository.existsByNombreJuego("EA FC 24")).thenReturn(false);
        when(juegoRepository.save(any(Juego.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JuegoResponse resultado = juegoService.actualizarJuego(1L, peticionActualizar);

        assertNotNull(resultado);
        assertEquals("EA FC 24", resultado.getNombreJuego());
        assertEquals(20000.0, resultado.getTotalPremio());
        verify(juegoRepository).findById(1L);
        verify(juegoRepository).save(any(Juego.class));
    }

    @Test
    void deberiaDesactivarAlJuegoPorId() {
        Juego juegoGuardado = Juego.builder()
                .id(1L)
                .activo(true)
                .build();

        when(juegoRepository.findById(1L)).thenReturn(Optional.of(juegoGuardado));

        juegoService.desactivarJuego(1L);

        assertFalse(juegoGuardado.getActivo());
        verify(juegoRepository).save(juegoGuardado);
    }

    // Tests ERRORES
    @Test
    void deberiaLanzarErrorPorNombreExistenteAlCrear() {
        JuegoRequest peticion = JuegoRequest.builder()
                .nombreJuego("Valorant")
                .build();

        when(juegoRepository.existsByNombreJuego("Valorant")).thenReturn(true);

        NombreDeJuegoExisteException ex = assertThrows(NombreDeJuegoExisteException.class,
                () -> juegoService.crearJuego(peticion));

        assertNotNull(ex);
        assertEquals("El Juego Valorant ya existe.", ex.getMessage());
        verify(juegoRepository, never()).save(any(Juego.class));
    }

    @Test
    void deberiaLanzarErrorPorJuegoNoEncontrado() {
        when(juegoRepository.findById(99L)).thenReturn(Optional.empty());

        JuegoNoEncontradoException ex = assertThrows(JuegoNoEncontradoException.class,
                () -> juegoService.buscarJuegoPorId(99L));

        assertNotNull(ex);
        assertEquals("Juego con la ID: 99 no encontrado", ex.getMessage());
        verify(juegoRepository).findById(99L);
    }

    @Test
    void deberiaLanzarJuegoNoEncontradoAlActualizar() {
        JuegoRequest peticion = JuegoRequest.builder()
                .nombreJuego("Smite")
                .build();

        when(juegoRepository.findById(99L)).thenReturn(Optional.empty());

        JuegoNoEncontradoException ex = assertThrows(JuegoNoEncontradoException.class,
                () -> juegoService.actualizarJuego(99L, peticion));

        assertNotNull(ex);
        assertEquals("Juego con la ID: 99 no encontrado", ex.getMessage());
        verify(juegoRepository).findById(99L);
        verify(juegoRepository, never()).save(any(Juego.class));
    }

    @Test
    void deberiaLanzarNombreDuplicadoAlActualizar() {
        Juego juegoGuardado = Juego.builder()
                .id(1L)
                .nombreJuego("Rocket League")
                .build();

        JuegoRequest peticionDuplicado = JuegoRequest.builder()
                .nombreJuego("Valorant")
                .build();

        when(juegoRepository.findById(1L)).thenReturn(Optional.of(juegoGuardado));
        when(juegoRepository.existsByNombreJuego("Valorant")).thenReturn(true);

        NombreDeJuegoExisteException ex = assertThrows(NombreDeJuegoExisteException.class,
                () -> juegoService.actualizarJuego(1L, peticionDuplicado));

        assertNotNull(ex);
        assertEquals("El Juego Valorant ya existe.", ex.getMessage());
        verify(juegoRepository).findById(1L);
        verify(juegoRepository, never()).save(any(Juego.class));
    }

    @Test
    void deberiaLanzarJuegoNoEncontradoAlDesactivar() {
        when(juegoRepository.findById(99L)).thenReturn(Optional.empty());

        JuegoNoEncontradoException ex = assertThrows(JuegoNoEncontradoException.class,
                () -> juegoService.desactivarJuego(99L));

        assertNotNull(ex);
        assertEquals("Juego con la ID: 99 no encontrado", ex.getMessage());
        verify(juegoRepository).findById(99L);
        verify(juegoRepository, never()).save(any(Juego.class));
    }

}
