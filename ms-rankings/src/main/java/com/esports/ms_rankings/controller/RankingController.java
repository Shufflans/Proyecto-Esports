package com.esports.ms_rankings.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esports.ms_rankings.dto.ApiResponse;
import com.esports.ms_rankings.dto.RankingRequest;
import com.esports.ms_rankings.dto.RankingResponse;
import com.esports.ms_rankings.service.RankingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Rankings", description = "Operaciones relacionadas con los Rankings de Jugadores y Equipos")
@RestController
@RequestMapping("/api/v1/rankings")
@RequiredArgsConstructor
@Slf4j
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "Registrar o Actualizar Ranking", description = "Crea un nuevo ranking o actualiza los puntos de un jugador si ya existe")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Ranking registrado con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Jugador o equipo no encontrados")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RankingResponse>> registrarRanking(
            @Valid @RequestBody RankingRequest rr,
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {

        log.info("POST /api/v1/rankings - Registrando ranking para jugador ID: {}", rr.getIdJugador());

        RankingResponse rankingCreado = rankingService.registrarRanking(rr, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<RankingResponse>builder()
                        .success(true)
                        .message("Ranking registrado con éxito")
                        .data(rankingCreado)
                        .build());
    }

    @Operation(summary = "Listar todos los Rankings", description = "Muestra todos los rankings registrados en el sistema")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rankings encontrados")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<RankingResponse>>> mostrarTodosLosRankings() {
        log.info("GET /api/v1/rankings - Mostrando todos los rankings");

        List<RankingResponse> rankingsEncontrados = rankingService.listarTodosLosRankings();

        return ResponseEntity.ok().body(
                ApiResponse.<List<RankingResponse>>builder()
                        .success(true)
                        .message("Rankings encontrados")
                        .data(rankingsEncontrados)
                        .build());
    }

    @Operation(summary = "Buscar Rankings por ID de Jugador", description = "Busca todos los rankings asociados a la ID de un jugador")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rankings del jugador encontrados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rankings no encontrados")
    })
    @GetMapping("/jugador/{id}")
    public ResponseEntity<ApiResponse<List<RankingResponse>>> buscarRankingsPorJugador(
            @Parameter(description = "ID del jugador a buscar", example = "1", required = true) @PathVariable Long id) {

        log.info("GET /api/v1/rankings/jugador/{} - Buscar Rankings por Jugador", id);

        List<RankingResponse> rankingsEncontrados = rankingService.listarRankingsPorJugador(id);

        return ResponseEntity.ok().body(
                ApiResponse.<List<RankingResponse>>builder()
                        .success(true)
                        .message("Rankings del jugador encontrados")
                        .data(rankingsEncontrados)
                        .build());
    }

    @Operation(summary = "Buscar Rankings por ID de Equipo", description = "Busca todos los rankings asociados a la ID de un equipo")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rankings del equipo encontrados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rankings no encontrados")
    })
    @GetMapping("/equipo/{id}")
    public ResponseEntity<ApiResponse<List<RankingResponse>>> buscarRankingsPorEquipo(
            @Parameter(description = "ID del equipo a buscar", example = "1", required = true) @PathVariable Long id) {

        log.info("GET /api/v1/rankings/equipo/{} - Buscar Rankings por Equipo", id);

        List<RankingResponse> rankingsEncontrados = rankingService.listarRankingsPorEquipo(id);

        return ResponseEntity.ok().body(
                ApiResponse.<List<RankingResponse>>builder()
                        .success(true)
                        .message("Rankings del equipo encontrados")
                        .data(rankingsEncontrados)
                        .build());
    }

    @Operation(summary = "Eliminar Ranking", description = "Elimina un ranking específico usando su ID principal")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ranking eliminado con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ranking no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminarRanking(
            @Parameter(description = "ID del ranking a eliminar", example = "1", required = true) @PathVariable Long id) {

        log.info("DELETE /api/v1/rankings/{} - Eliminando Ranking", id);

        rankingService.eliminarRanking(id);

        return ResponseEntity.ok().body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Ranking eliminado con éxito")
                        .data(null)
                        .build());
    }
}