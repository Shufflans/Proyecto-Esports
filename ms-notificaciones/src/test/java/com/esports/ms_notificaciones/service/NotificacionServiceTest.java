package com.esports.ms_notificaciones.service;

import com.esports.ms_notificaciones.client.JugadorClient;
import com.esports.ms_notificaciones.dto.JugadorResponse;
import com.esports.ms_notificaciones.dto.NotificacionRequest;
import com.esports.ms_notificaciones.dto.NotificacionResponse;
import com.esports.ms_notificaciones.exception.JugadorNoExisteException;
import com.esports.ms_notificaciones.exception.NotificacionNoExisteException;
import com.esports.ms_notificaciones.exception.NotificacionYaLeidaException;
import com.esports.ms_notificaciones.model.Notificacion;
import com.esports.ms_notificaciones.repository.NotificacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

        @Mock
        private NotificacionRepository notificacionRepository;

        @Mock
        private JugadorClient jugadorClient;

        @InjectMocks
        private NotificacionService service;

        @Test
        void deberiaRetornarNotificacionSiExiste() {
                Notificacion notificacion = Notificacion.builder()
                                .id(1L)
                                .idJugador(5L)
                                .nombreJugador("nakaseomyces")
                                .asunto("Bienvenida")
                                .mensaje("Hola, bienvenido al torneo")
                                .estado("NO_LEIDA")
                                .fechaCreacion(LocalDateTime.now())
                                .build();
                when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

                NotificacionResponse resultado = service.obtenerPorId(1L);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals("nakaseomyces", resultado.getNombreJugador());
                verify(notificacionRepository).findById(1L);
        }

        @Test
        void deberiaRetornarListaDeNotificaciones() {
                Notificacion notificacion = Notificacion.builder()
                                .id(1L)
                                .asunto("Aviso de Partido")
                                .build();

                when(notificacionRepository.findAll()).thenReturn(List.of(notificacion));

                List<NotificacionResponse> resultado = service.listarTodas();

                assertFalse(resultado.isEmpty());
                assertEquals(1, resultado.size());
                assertEquals("Aviso de Partido", resultado.get(0).getAsunto());
                verify(notificacionRepository).findAll();
        }

        @Test
        void deberiaRetornarListaDeNotificacionesPorJugador() {
                Notificacion notificacion = Notificacion.builder()
                                .id(1L)
                                .idJugador(5L)
                                .asunto("Alerta")
                                .build();

                when(notificacionRepository.findByIdJugador(5L)).thenReturn(List.of(notificacion));

                List<NotificacionResponse> resultado = service.listarPorJugador(5L);

                assertFalse(resultado.isEmpty());
                assertEquals(1, resultado.size());
                assertEquals(5L, resultado.get(0).getIdJugador());
                verify(notificacionRepository).findByIdJugador(5L);
        }

        @Test
        void deberiaCrearUnaNotificacion() {
                NotificacionRequest request = NotificacionRequest.builder()
                                .idJugador(5L)
                                .asunto("Sanción")
                                .mensaje("Has sido sancionado por inactividad")
                                .build();

                JugadorResponse jugadorMock = new JugadorResponse();
                jugadorMock.setId(5L);
                jugadorMock.setNombre("nakaseomyces");

                Notificacion notificacionGuardada = Notificacion.builder()
                                .id(1L)
                                .idJugador(5L)
                                .nombreJugador("nakaseomyces")
                                .asunto("Sanción")
                                .mensaje("Has sido sancionado por inactividad")
                                .estado("NO_LEIDA")
                                .fechaCreacion(LocalDateTime.now())
                                .build();

                when(jugadorClient.obtenerJugador(eq(5L), anyString())).thenReturn(jugadorMock);
                when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionGuardada);

                NotificacionResponse resultado = service.crearNotificacion(request, "token123");

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(5L, resultado.getIdJugador());
                assertEquals("nakaseomyces", resultado.getNombreJugador());
                assertEquals("Sanción", resultado.getAsunto());
                assertEquals("NO_LEIDA", resultado.getEstado());
                verify(jugadorClient).obtenerJugador(5L, "token123");
                verify(notificacionRepository).save(any(Notificacion.class));
        }

        @Test
        void deberiaMarcarComoLeida() {
                Notificacion notificacionGuardada = Notificacion.builder()
                                .id(1L)
                                .estado("NO_LEIDA")
                                .build();

                when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionGuardada));
                when(notificacionRepository.save(any(Notificacion.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                NotificacionResponse resultado = service.marcarComoLeida(1L);

                assertEquals("LEIDA", resultado.getEstado());
                verify(notificacionRepository).findById(1L);
                verify(notificacionRepository).save(any(Notificacion.class));
        }

        // TESTS PARA LOS ERRORES
        @Test
        void deberiaLanzarJugadorNoExisteAlCrear() {
                NotificacionRequest request = NotificacionRequest.builder()
                                .idJugador(99L)
                                .build();

                when(jugadorClient.obtenerJugador(eq(99L), anyString())).thenReturn(null);

                JugadorNoExisteException ex = assertThrows(JugadorNoExisteException.class,
                                () -> service.crearNotificacion(request, "token123"));

                assertNotNull(ex);
                assertEquals("El jugador con el ID 99 no existe", ex.getMessage());
                verify(jugadorClient).obtenerJugador(99L, "token123");
                verify(notificacionRepository, never()).save(any(Notificacion.class));
        }

        @Test
        void deberiaLanzarNotificacionNoExistePorId() {
                when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

                NotificacionNoExisteException ex = assertThrows(NotificacionNoExisteException.class,
                                () -> service.obtenerPorId(99L));

                assertNotNull(ex);
                assertEquals("La notificación con el ID 99 no existe", ex.getMessage());
                verify(notificacionRepository).findById(99L);
        }

        @Test
        void deberiaLanzarNotificacionNoExisteAlMarcarComoLeida() {
                when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

                NotificacionNoExisteException ex = assertThrows(NotificacionNoExisteException.class,
                                () -> service.marcarComoLeida(99L));

                assertNotNull(ex);
                assertEquals("La notificación con el ID 99 no existe", ex.getMessage());
                verify(notificacionRepository).findById(99L);
                verify(notificacionRepository, never()).save(any(Notificacion.class));
        }

        @Test
        void deberiaLanzarNotificacionYaLeida() {
                Notificacion notificacionGuardada = Notificacion.builder()
                                .id(1L)
                                .estado("LEIDA")
                                .build();

                when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionGuardada));

                NotificacionYaLeidaException ex = assertThrows(NotificacionYaLeidaException.class,
                                () -> service.marcarComoLeida(1L));

                assertNotNull(ex);
                assertEquals("Esta notificación ya está marcada como leída", ex.getMessage());
                verify(notificacionRepository).findById(1L);
                verify(notificacionRepository, never()).save(any(Notificacion.class));
        }
}