package com.esports.ms_estadisticas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esports.ms_estadisticas.dto.ApiResponse;
import com.esports.ms_estadisticas.dto.EstadisticaRequest;
import com.esports.ms_estadisticas.dto.EstadisticaResponse;
import com.esports.ms_estadisticas.service.EstadisticaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Estadisticas", description = "Operaciones relacionadas con las estadísticas de las partidas")
@RestController
@RequestMapping("/api/v1/estadisticas")
@RequiredArgsConstructor
@Slf4j
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    @Operation(summary = "Crear Estadística", description = "Crea una estadística para un jugador dentro de una partida")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Estadística creada con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El jugador o la partida no existen"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "La estadística ya existe")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EstadisticaResponse>> crearEstadistica(
            @Valid @RequestBody EstadisticaRequest er, @RequestHeader("Authorization") String token) {
        log.info("POST /api/v1/estadisticas - Creando Estadística para el jugador ID: {}", er.getIdJugador());

        EstadisticaResponse estadisticaRecuperada = estadisticaService.crearEstadistica(er, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<EstadisticaResponse>builder()
                        .success(true)
                        .message("Estadística creada con éxito")
                        .data(estadisticaRecuperada)
                        .build());
    }

    @Operation(summary = "Buscar Estadísticas por Jugador", description = "Busca todas las estadísticas asociadas a un jugador específico por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estadísticas Encontradas"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Estadísticas no encontradas")
    })
    @GetMapping("/jugador/{idJugador}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<EstadisticaResponse>>> buscarEstadisticasPorJugador(
            @Parameter(description = "ID del Jugador a buscar", example = "1", required = true) @PathVariable Long idJugador) {
        log.info("GET /api/v1/estadisticas/jugador/{} - Buscar Estadísticas por ID de Jugador", idJugador);

        List<EstadisticaResponse> estadisticasEncontradas = estadisticaService.listarEstadisticasPorJugador(idJugador);

        return ResponseEntity.ok().body(
                ApiResponse.<List<EstadisticaResponse>>builder()
                        .success(true)
                        .message("Estadísticas del Jugador Encontradas")
                        .data(estadisticasEncontradas)
                        .build());
    }

    @Operation(summary = "Buscar Estadísticas por Partida", description = "Busca todas las estadísticas asociadas a una partida específica por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estadísticas Encontradas"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Estadísticas no encontradas")
    })
    @GetMapping("/partida/{idPartida}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<EstadisticaResponse>>> buscarEstadisticasPorPartida(
            @Parameter(description = "ID de la Partida a buscar", example = "1", required = true) @PathVariable Long idPartida) {
        log.info("GET /api/v1/estadisticas/partida/{} - Buscar Estadísticas por ID de Partida", idPartida);

        List<EstadisticaResponse> estadisticasEncontradas = estadisticaService.listarEstadisticasPorPartida(idPartida);

        return ResponseEntity.ok().body(
                ApiResponse.<List<EstadisticaResponse>>builder()
                        .success(true)
                        .message("Estadísticas de la Partida Encontradas")
                        .data(estadisticasEncontradas)
                        .build());
    }

}