package com.esports.ms_transferencias.service;

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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.esports.ms_transferencias.client.EquipoClient;
import com.esports.ms_transferencias.client.JugadorClient;
import com.esports.ms_transferencias.dto.EquipoResponse;
import com.esports.ms_transferencias.dto.JugadorResponse;
import com.esports.ms_transferencias.dto.TransferenciaRequest;
import com.esports.ms_transferencias.dto.TransferenciaResponse;
import com.esports.ms_transferencias.exception.EquipoNoEncontradoException;
import com.esports.ms_transferencias.exception.JugadorNoExisteException;
import com.esports.ms_transferencias.exception.TipoDeTransferenciaInvalidoException;
import com.esports.ms_transferencias.model.Transferencia;
import com.esports.ms_transferencias.repository.TransferenciaRepository;

@ExtendWith(MockitoExtension.class)
public class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @Mock
    private JugadorClient jugadorClient;

    @Mock
    private EquipoClient equipoClient;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @Test
    void deberiaRegistrarFichajeInicial() {
        TransferenciaRequest peticion = TransferenciaRequest.builder()
                .idJugador(1L)
                .tipo("FICHAJE_INICIAL")
                .idEquipoDestino(10L)
                .montoUsd(50000.0)
                .fechaTransferencia(LocalDate.now())
                .build();

        JugadorResponse jugador = JugadorResponse.builder().id(1L).nickname("Faker").build();
        EquipoResponse equipoDestino = EquipoResponse.builder().id(10L).nombreEquipo("T1").build();

        when(jugadorClient.obtenerJugador(1L, "token")).thenReturn(jugador);
        when(equipoClient.obtenerEquipo(10L, "token")).thenReturn(equipoDestino);
        when(transferenciaRepository.save(any(Transferencia.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransferenciaResponse resultado = transferenciaService.registrarTransferencia(peticion, "token");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdJugador());
        assertEquals("Faker", resultado.getNicknameJugador());
        assertEquals(10L, resultado.getIdEquipoDestino());
        assertEquals("T1", resultado.getNombreEquipoDestino());
        assertEquals("FICHAJE_INICIAL", resultado.getTipo());

        verify(jugadorClient).obtenerJugador(1L, "token");
        verify(equipoClient).obtenerEquipo(10L, "token");
        verify(transferenciaRepository).save(any(Transferencia.class));
    }

    @Test
    void deberiaRegistrarBaja() {
        TransferenciaRequest peticion = TransferenciaRequest.builder()
                .idJugador(2L)
                .tipo("BAJA")
                .idEquipoOrigen(20L)
                .build();

        JugadorResponse jugador = JugadorResponse.builder().id(2L).nickname("Caps").build();
        EquipoResponse equipoOrigen = EquipoResponse.builder().id(20L).nombreEquipo("G2 Esports").build();

        when(jugadorClient.obtenerJugador(2L, "token")).thenReturn(jugador);
        when(equipoClient.obtenerEquipo(20L, "token")).thenReturn(equipoOrigen);
        when(transferenciaRepository.save(any(Transferencia.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransferenciaResponse resultado = transferenciaService.registrarTransferencia(peticion, "token");

        assertNotNull(resultado);
        assertEquals(20L, resultado.getIdEquipoOrigen());
        assertEquals("G2 Esports", resultado.getNombreEquipoOrigen());
        assertEquals("BAJA", resultado.getTipo());

        verify(jugadorClient).obtenerJugador(2L, "token");
        verify(equipoClient).obtenerEquipo(20L, "token");
        verify(transferenciaRepository).save(any(Transferencia.class));
    }

    @Test
    void deberiaRegistrarTransferenciaOPrestamo() {
        TransferenciaRequest peticion = TransferenciaRequest.builder()
                .idJugador(3L)
                .tipo("TRANSFERENCIA")
                .idEquipoOrigen(10L)
                .idEquipoDestino(20L)
                .build();

        JugadorResponse jugador = JugadorResponse.builder().id(3L).nickname("Chovy").build();
        EquipoResponse equipoOrigen = EquipoResponse.builder().id(10L).nombreEquipo("Gen.G").build();
        EquipoResponse equipoDestino = EquipoResponse.builder().id(20L).nombreEquipo("Hanwha Life").build();

        when(jugadorClient.obtenerJugador(3L, "token")).thenReturn(jugador);
        when(equipoClient.obtenerEquipo(10L, "token")).thenReturn(equipoOrigen);
        when(equipoClient.obtenerEquipo(20L, "token")).thenReturn(equipoDestino);
        when(transferenciaRepository.save(any(Transferencia.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransferenciaResponse resultado = transferenciaService.registrarTransferencia(peticion, "token");

        assertNotNull(resultado);
        assertEquals("Gen.G", resultado.getNombreEquipoOrigen());
        assertEquals("Hanwha Life", resultado.getNombreEquipoDestino());
        assertEquals("TRANSFERENCIA", resultado.getTipo());

        verify(jugadorClient).obtenerJugador(3L, "token");
        verify(equipoClient).obtenerEquipo(10L, "token");
        verify(equipoClient).obtenerEquipo(20L, "token");
        verify(transferenciaRepository).save(any(Transferencia.class));
    }

    @Test
    void deberiaRetornarListaDeTransferencias() {
        Transferencia transferencia = new Transferencia();
        transferencia.setId(1L);
        transferencia.setTipo("PRESTAMO");

        when(transferenciaRepository.findAll()).thenReturn(List.of(transferencia));

        List<TransferenciaResponse> resultado = transferenciaService.listarTodasLasTransferencias();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("PRESTAMO", resultado.get(0).getTipo());
        verify(transferenciaRepository).findAll();
    }

    @Test
    void deberiaRetornarTransferenciasPorJugador() {
        Transferencia transferencia = new Transferencia();
        transferencia.setIdJugador(5L);
        transferencia.setNicknameJugador("Rookie");

        when(transferenciaRepository.findByIdJugador(5L)).thenReturn(List.of(transferencia));

        List<TransferenciaResponse> resultado = transferenciaService.buscarTransferenciasPorJugador(5L);

        assertFalse(resultado.isEmpty());
        assertEquals("Rookie", resultado.get(0).getNicknameJugador());
        verify(transferenciaRepository).findByIdJugador(5L);
    }

    @Test
    void deberiaRetornarTransferenciasPorTipo() {
        Transferencia transferencia = new Transferencia();
        transferencia.setTipo("FICHAJE_INICIAL");

        when(transferenciaRepository.findByTipo("FICHAJE_INICIAL")).thenReturn(List.of(transferencia));

        List<TransferenciaResponse> resultado = transferenciaService.buscarTransferenciasPorTipo("FICHAJE_INICIAL");

        assertFalse(resultado.isEmpty());
        assertEquals("FICHAJE_INICIAL", resultado.get(0).getTipo());
        verify(transferenciaRepository).findByTipo("FICHAJE_INICIAL");
    }

    // Tests ERRORES
    @Test
    void deberiaLanzarJugadorNoExisteAlRegistrar() {
        TransferenciaRequest peticion = TransferenciaRequest.builder().idJugador(99L).build();

        when(jugadorClient.obtenerJugador(99L, "token")).thenReturn(null);

        JugadorNoExisteException ex = assertThrows(JugadorNoExisteException.class,
                () -> transferenciaService.registrarTransferencia(peticion, "token"));

        assertNotNull(ex);
        assertEquals("Jugador con la ID: 99 no encontrado", ex.getMessage());
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    void deberiaLanzarTipoInvalidoFichajeInicialSinDestino() {
        TransferenciaRequest peticion = TransferenciaRequest.builder()
                .idJugador(1L)
                .tipo("FICHAJE_INICIAL")
                .build();

        when(jugadorClient.obtenerJugador(1L, "token")).thenReturn(JugadorResponse.builder().id(1L).build());

        TipoDeTransferenciaInvalidoException ex = assertThrows(TipoDeTransferenciaInvalidoException.class,
                () -> transferenciaService.registrarTransferencia(peticion, "token"));

        assertNotNull(ex);
        assertEquals("Un fichaje inicial requiere un equipo destino.", ex.getMessage());
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    void deberiaLanzarEquipoNoEncontradoFichajeInicial() {
        TransferenciaRequest peticion = TransferenciaRequest.builder()
                .idJugador(1L)
                .tipo("FICHAJE_INICIAL")
                .idEquipoDestino(99L)
                .build();

        when(jugadorClient.obtenerJugador(1L, "token")).thenReturn(JugadorResponse.builder().id(1L).build());
        when(equipoClient.obtenerEquipo(99L, "token")).thenReturn(null);

        EquipoNoEncontradoException ex = assertThrows(EquipoNoEncontradoException.class,
                () -> transferenciaService.registrarTransferencia(peticion, "token"));

        assertNotNull(ex);
        assertEquals("El equipo con ID: 99 no encontrado", ex.getMessage());
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    void deberiaLanzarTipoInvalidoTransferenciaSinOrigenODestino() {
        TransferenciaRequest peticion = TransferenciaRequest.builder()
                .idJugador(1L)
                .tipo("TRANSFERENCIA")
                .idEquipoOrigen(10L)
                .build();

        when(jugadorClient.obtenerJugador(1L, "token")).thenReturn(JugadorResponse.builder().id(1L).build());

        TipoDeTransferenciaInvalidoException ex = assertThrows(TipoDeTransferenciaInvalidoException.class,
                () -> transferenciaService.registrarTransferencia(peticion, "token"));

        assertNotNull(ex);
        assertEquals("Este tipo de movimiento requiere equipo origen y destino.", ex.getMessage());
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    void deberiaLanzarTipoInvalidoGeneral() {
        TransferenciaRequest peticion = TransferenciaRequest.builder()
                .idJugador(1L)
                .tipo("CUALQUIER_COSA")
                .build();

        when(jugadorClient.obtenerJugador(1L, "token")).thenReturn(JugadorResponse.builder().id(1L).build());

        TipoDeTransferenciaInvalidoException ex = assertThrows(TipoDeTransferenciaInvalidoException.class,
                () -> transferenciaService.registrarTransferencia(peticion, "token"));

        assertNotNull(ex);
        assertEquals("El tipo CUALQUIER_COSA no es un tipo de transferencia válido.", ex.getMessage());
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }
}