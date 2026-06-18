package com.esports.ms_notificaciones.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.esports.ms_notificaciones.dto.ApiResponse;
import com.esports.ms_notificaciones.dto.NotificacionRequest;
import com.esports.ms_notificaciones.dto.NotificacionResponse;
import com.esports.ms_notificaciones.service.NotificacionService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionControllerTest {

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private NotificacionController controller;

    @Test
    void deberiaCrearUnaNotificacion() {
        NotificacionRequest request = NotificacionRequest.builder()
                .idJugador(5L)
                .asunto("Aviso de Torneo")
                .mensaje("El torneo empieza mañana")
                .build();

        NotificacionResponse notificacionCreada = NotificacionResponse.builder()
                .id(1L)
                .idJugador(5L)
                .nombreJugador("nakaseomyces")
                .asunto("Aviso de Torneo")
                .mensaje("El torneo empieza mañana")
                .estado("NO_LEIDA")
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(notificacionService.crearNotificacion(any(NotificacionRequest.class), eq("token123")))
                .thenReturn(notificacionCreada);

        ResponseEntity<ApiResponse<NotificacionResponse>> resultado = controller.crearNotificacion(request, "token123");

        assertNotNull(resultado);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertTrue(resultado.getBody().isSuccess());
        assertEquals("Notificación creada con éxito", resultado.getBody().getMessage());
        assertEquals(1L, resultado.getBody().getData().getId());
        assertEquals("Aviso de Torneo", resultado.getBody().getData().getAsunto());
        verify(notificacionService).crearNotificacion(any(NotificacionRequest.class), eq("token123"));
    }

    @Test
    void deberiaMarcarComoLeida() {
        NotificacionResponse notificacionActualizada = NotificacionResponse.builder()
                .id(1L)
                .estado("LEIDA")
                .build();

        when(notificacionService.marcarComoLeida(1L)).thenReturn(notificacionActualizada);

        ResponseEntity<ApiResponse<NotificacionResponse>> resultado = controller.marcarComoLeida(1L);

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertTrue(resultado.getBody().isSuccess());
        assertEquals("Notificación marcada como leída con éxito", resultado.getBody().getMessage());
        assertEquals("LEIDA", resultado.getBody().getData().getEstado());
        verify(notificacionService).marcarComoLeida(1L);
    }

    @Test
    void deberiaObtenerPorId() {
        NotificacionResponse notificacionEncontrada = NotificacionResponse.builder()
                .id(1L)
                .idJugador(5L)
                .asunto("Bienvenida")
                .build();

        when(notificacionService.obtenerPorId(1L)).thenReturn(notificacionEncontrada);

        ResponseEntity<ApiResponse<NotificacionResponse>> resultado = controller.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertTrue(resultado.getBody().isSuccess());
        assertEquals("Notificación encontrada", resultado.getBody().getMessage());
        assertEquals(1L, resultado.getBody().getData().getId());
        verify(notificacionService).obtenerPorId(1L);
    }

    @Test
    void deberiaListarTodas() {
        NotificacionResponse notificacion = NotificacionResponse.builder()
                .id(1L)
                .asunto("Aviso")
                .build();

        when(notificacionService.listarTodas()).thenReturn(List.of(notificacion));

        ResponseEntity<ApiResponse<List<NotificacionResponse>>> resultado = controller.listarTodas();

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertTrue(resultado.getBody().isSuccess());
        assertEquals("Notificaciones listadas con éxito", resultado.getBody().getMessage());
        assertFalse(resultado.getBody().getData().isEmpty());
        assertEquals(1, resultado.getBody().getData().size());
        verify(notificacionService).listarTodas();
    }

    @Test
    void deberiaListarPorJugador() {
        NotificacionResponse notificacion = NotificacionResponse.builder()
                .id(1L)
                .idJugador(5L)
                .asunto("Sanción")
                .build();

        when(notificacionService.listarPorJugador(5L)).thenReturn(List.of(notificacion));

        ResponseEntity<ApiResponse<List<NotificacionResponse>>> resultado = controller.listarPorJugador(5L);

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertTrue(resultado.getBody().isSuccess());
        assertEquals("Notificaciones por Jugador listadas", resultado.getBody().getMessage());
        assertEquals(5L, resultado.getBody().getData().get(0).getIdJugador());
        verify(notificacionService).listarPorJugador(5L);
    }
}