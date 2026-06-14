package com.esports.ms_jugadores.service;

import com.esports.ms_jugadores.dto.JugadorRequest;
import com.esports.ms_jugadores.dto.JugadorResponse;
import com.esports.ms_jugadores.exception.EdadMinimaException;
import com.esports.ms_jugadores.exception.JugadorNoEncontradoException;
import com.esports.ms_jugadores.exception.NickNameDuplicadoException;
import com.esports.ms_jugadores.model.Jugador;
import com.esports.ms_jugadores.repository.JugadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    @Mock
    private JugadorRepository repo;

    @InjectMocks
    private JugadorService service;

    @Test
    void deberiaRetornarJugadorSiExiste() {
        Jugador jugador = Jugador.builder()
                .id(1L)
                .build();
        when(repo.findById(1L)).thenReturn(Optional.of(jugador));

        JugadorResponse resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repo).findById(1L);
    }

    @Test
    void deberiaRetornarListaDeJugadores() {
        Jugador jugador = Jugador.builder()
                .id(1L)
                .nickname("nakaseomyces")
                .build();

        when(repo.findAll()).thenReturn(List.of(jugador));

        List<JugadorResponse> resultado = service.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("nakaseomyces", resultado.get(0).getNickname());
        verify(repo).findAll();
    }

    @Test
    void deberiaCrearUnJugador() {
        JugadorRequest jugador = JugadorRequest.builder()
                .nickname("nakaseomyces")
                .nombreReal("Perla Valentina")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-05-02"))
                .rol("USER")
                .idEquipoActual(null)
                .salarioMensual(100000.0)
                .build();

        Jugador jugadorGuardado = Jugador.builder()
                .id(1L)
                .nickname("nakaseomyces")
                .nombreReal("Perla Valentina")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-05-02"))
                .rol("USER")
                .idEquipoActual(null)
                .activo(true)
                .salarioMensual(100000.0)
                .build();

        when(repo.save(any(Jugador.class))).thenReturn(jugadorGuardado);

        JugadorResponse resultado = service.crear(jugador);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("nakaseomyces", resultado.getNickname());
        assertEquals("Perla Valentina", resultado.getNombreReal());
        assertEquals("Chile", resultado.getPais());
        assertEquals(LocalDate.parse("2000-05-02"), resultado.getFechaNacimiento());
        assertEquals("USER", resultado.getRol());
        assertEquals(null, resultado.getIdEquipoActual());
        assertEquals(true, resultado.getActivo());
        assertEquals(100000.0, resultado.getSalarioMensual());
        verify(repo).save(any(Jugador.class));
    }

    @Test
    void deberiaActualizarJugador() {
        Jugador jugadorGuardado = Jugador.builder()
                .id(1L)
                .nickname("nakaseomyces")
                .nombreReal("Perla Valentina Antiñir")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-05-02"))
                .rol("USER")
                .idEquipoActual(null)
                .activo(true)
                .salarioMensual(100000.0)
                .build();

        JugadorRequest jugador = JugadorRequest.builder()
                .nickname("nakaseomyces")
                .nombreReal("Perla Valentina Antiñir")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-05-02"))
                .rol("USER")
                .idEquipoActual(null)
                .salarioMensual(100000.0)
                .build();

        when(repo.findById(1L)).thenReturn(Optional.of(jugadorGuardado));
        when(repo.save(any(Jugador.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JugadorResponse resultado = service.actualizar(1L, jugador);

        assertEquals(1L, resultado.getId());
        assertEquals("nakaseomyces", resultado.getNickname());
        assertEquals("Perla Valentina Antiñir", resultado.getNombreReal());
        assertEquals("Chile", resultado.getPais());
        assertEquals(LocalDate.parse("2000-05-02"), resultado.getFechaNacimiento());
        assertEquals("USER", resultado.getRol());
        assertEquals(null, resultado.getIdEquipoActual());
        assertEquals(true, resultado.getActivo());
        assertEquals(100000.0, resultado.getSalarioMensual());
        verify(repo).findById(1L);
    }

    @Test
    void deberiaDesactivarAlJugadorPorId() {
        Jugador jugadorGuardado = Jugador.builder()
                .id(1L)
                .activo(true)
                .build();

        when(repo.findById(1L)).thenReturn(Optional.of(jugadorGuardado));

        service.desactivarJugador(1L);

        assertFalse(jugadorGuardado.getActivo());
        verify(repo).save(jugadorGuardado);
    }

    // TESTS PARA LOS ERRORES
    @Test
    void deberiaLanzarErrorPorNombreExistente() {
        JugadorRequest jugadorNuevo = JugadorRequest.builder()
                .nickname("nakaseomyces")
                .build();

        when(repo.existsByNickname("nakaseomyces")).thenReturn(true);

        NickNameDuplicadoException ex = assertThrows(NickNameDuplicadoException.class,
                () -> service.crear(jugadorNuevo));

        assertNotNull(ex);
        assertEquals("No se pudo registrar, el nickname nakaseomyces ya existe.", ex.getMessage());
        verify(repo, never()).save(any(Jugador.class));
    }

    @Test
    void deberiaLanzarErrorPorEdadMinima() {
        JugadorRequest jugadorMenor = JugadorRequest.builder()
                .fechaNacimiento(LocalDate.parse("2020-02-05"))
                .build();

        EdadMinimaException ex = assertThrows(EdadMinimaException.class, () -> service.crear(jugadorMenor));

        assertNotNull(ex);
        assertEquals("La edad minima debe ser igual o mayor a 16", ex.getMessage());
        verify(repo, never()).save(any(Jugador.class));
    }

    @Test
    void deberiaLanzarJugadorNoEncontradoPorId() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        JugadorNoEncontradoException ex = assertThrows(JugadorNoEncontradoException.class,
                () -> service.buscarPorId(99L));
        assertNotNull(ex);
        assertEquals("Jugador con la id 99 no encontrado", ex.getMessage());
        verify(repo).findById(99L);
    }

    @Test
    void deberiaLanzarJugadorNoEncontradoAlActualizar() {
        JugadorRequest jugador = JugadorRequest.builder()
                .nickname("nakaseomyces")
                .build();
        when(repo.findById(99L)).thenReturn(Optional.empty());

        JugadorNoEncontradoException ex = assertThrows(JugadorNoEncontradoException.class,
                () -> service.actualizar(99L, jugador));

        assertNotNull(ex);
        assertEquals("Jugador con la ID 99 no encontrado", ex.getMessage());
        verify(repo).findById(99L);
    }

    @Test
    void deberiaLanzarNombreDuplicadoAlActualizar() {
        Jugador jugadorGuardado = Jugador.builder()
                .id(1L)
                .nickname("Shufflan")
                .build();

        JugadorRequest jugadorDuplicado = JugadorRequest.builder()
                .nickname("nakaseomyces")
                .build();

        when(repo.findById(1L)).thenReturn(Optional.of(jugadorGuardado));
        when(repo.existsByNickname("nakaseomyces")).thenReturn(true);

        NickNameDuplicadoException ex = assertThrows(NickNameDuplicadoException.class,
                () -> service.actualizar(1L, jugadorDuplicado));

        assertNotNull(ex);
        assertEquals("El nickname: nakaseomyces ya existe", ex.getMessage());
        verify(repo).findById(1L);
    }

    @Test
    void deberiaLanzarJugadorNoEncontradoAlDesactivar() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        JugadorNoEncontradoException ex = assertThrows(JugadorNoEncontradoException.class,
                () -> service.desactivarJugador(99L));

        assertNotNull(ex);
        assertEquals("Jugador con ID: 99 no encontrado", ex.getMessage());
        verify(repo).findById(99L);
    }
}