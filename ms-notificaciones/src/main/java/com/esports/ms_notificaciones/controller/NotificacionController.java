package com.esports.ms_notificaciones.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esports.ms_notificaciones.dto.ApiResponse;
import com.esports.ms_notificaciones.dto.NotificacionRequest;
import com.esports.ms_notificaciones.dto.NotificacionResponse;
import com.esports.ms_notificaciones.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Notificaciones", description = "Operaciones relacionadas con las Notificaciones de jugadores")
@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
@Slf4j
public class NotificacionController {

    private final NotificacionService notificacionService;

    @Operation(summary = "Crear Notificación", description = "Crea una nueva notificación para un jugador conectándose con el microservicio de jugadores")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notificación creada con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El jugador ingresado no existe")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificacionResponse>> crearNotificacion(
            @Valid @RequestBody NotificacionRequest nr, @RequestHeader("Authorization") String token) {
        log.info("POST /api/v1/notificaciones - Creando Notificación para jugador ID: {}", nr.getIdJugador());

        NotificacionResponse notificacionCreada = notificacionService.crearNotificacion(nr, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<NotificacionResponse>builder()
                        .success(true)
                        .message("Notificación creada con éxito")
                        .data(notificacionCreada)
                        .build());
    }

    @Operation(summary = "Marcar Notificación como leída", description = "Actualiza el estado de una notificación específica a LEIDA")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notificación marcada como leída con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "La notificación ya se encuentra leída"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "La notificación ingresada no existe")
    })
    @PutMapping("/{id}/leida")
    public ResponseEntity<ApiResponse<NotificacionResponse>> marcarComoLeida(
            @Parameter(description = "ID de la notificación a actualizar", example = "1", required = true) @PathVariable Long id) {
        log.info("PUT /api/v1/notificaciones/{}/leida - Marcando Notificación como leída", id);

        NotificacionResponse notificacionActualizada = notificacionService.marcarComoLeida(id);

        return ResponseEntity.ok().body(
                ApiResponse.<NotificacionResponse>builder()
                        .success(true)
                        .message("Notificación marcada como leída con éxito")
                        .data(notificacionActualizada)
                        .build());
    }

    @Operation(summary = "Buscar Notificación por ID", description = "Muestra el detalle de una notificación específica usando su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notificación Encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "La notificación no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificacionResponse>> obtenerPorId(
            @Parameter(description = "ID de la notificación a buscar", example = "1", required = true) @PathVariable Long id) {
        log.info("GET /api/v1/notificaciones/{} - Buscando Notificación por ID", id);

        NotificacionResponse notificacionEncontrada = notificacionService.obtenerPorId(id);

        return ResponseEntity.ok().body(
                ApiResponse.<NotificacionResponse>builder()
                        .success(true)
                        .message("Notificación encontrada")
                        .data(notificacionEncontrada)
                        .build());
    }

    @Operation(summary = "Listar todas las Notificaciones", description = "Muestra el historial completo de todas las notificaciones registradas")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notificaciones Encontradas")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<NotificacionResponse>>> listarTodas() {
        log.info("GET /api/v1/notificaciones - Mostrando todas las Notificaciones");

        List<NotificacionResponse> notificacionesEncontradas = notificacionService.listarTodas();

        return ResponseEntity.ok().body(
                ApiResponse.<List<NotificacionResponse>>builder()
                        .success(true)
                        .message("Notificaciones listadas con éxito")
                        .data(notificacionesEncontradas)
                        .build());
    }

    @Operation(summary = "Listar Notificaciones por Jugador", description = "Muestra todo el historial de notificaciones asociado a un ID de jugador específico")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notificaciones Encontradas")
    })
    @GetMapping("/jugador/{id}")
    public ResponseEntity<ApiResponse<List<NotificacionResponse>>> listarPorJugador(
            @Parameter(description = "ID del jugador para filtrar sus notificaciones", example = "1", required = true) @PathVariable Long id) {
        log.info("GET /api/v1/notificaciones/jugador/{} - Mostrando Notificaciones por ID Jugador", id);

        List<NotificacionResponse> notificacionesEncontradas = notificacionService.listarPorJugador(id);

        return ResponseEntity.ok().body(
                ApiResponse.<List<NotificacionResponse>>builder()
                        .success(true)
                        .message("Notificaciones por Jugador listadas")
                        .data(notificacionesEncontradas)
                        .build());
    }
}