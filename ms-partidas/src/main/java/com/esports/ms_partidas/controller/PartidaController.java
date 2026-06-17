package com.esports.ms_partidas.controller;

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

import com.esports.ms_partidas.dto.ApiResponse;
import com.esports.ms_partidas.dto.PartidaRequest;
import com.esports.ms_partidas.dto.PartidaResponse;
import com.esports.ms_partidas.dto.ResultadoPartidaRequest;
import com.esports.ms_partidas.service.PartidaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Partidas", description = "Operaciones relacionadas con la creación de Partidas")
@RestController
@RequestMapping("/api/v1/partidas")
@RequiredArgsConstructor
@Slf4j
public class PartidaController {

        private final PartidaService partidaService;

        @Operation(summary = "Crear Partida", description = "Crea una nueva partida validando que el torneo y los equipos existan")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Partida creada con éxito"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Torneo o equipos no encontrados")
        })
        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<PartidaResponse>> crearPartida(
                        @Valid @RequestBody PartidaRequest pr,
                        @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
                log.info("POST /api/v1/partidas - Creando Partida para el torneo ID: {}", pr.getIdTorneo());

                PartidaResponse partidaCreada = partidaService.crearPartida(pr, token);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.<PartidaResponse>builder()
                                                .success(true)
                                                .message("Partida creada con éxito")
                                                .data(partidaCreada)
                                                .build());
        }

        @Operation(summary = "Finalizar Partida", description = "Registra el marcador final y establece el equipo ganador")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Partida finalizada con éxito"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos o ganador inválido"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "La partida no existe")
        })
        @PutMapping("/{id}/finalizar")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<PartidaResponse>> finalizarPartida(
                        @Parameter(description = "ID de la partida a finalizar", example = "1", required = true) @PathVariable Long id,
                        @Valid @RequestBody ResultadoPartidaRequest rpr) {
                log.info("PUT /api/v1/partidas/{}/finalizar - Finalizando Partida", id);

                PartidaResponse partidaFinalizada = partidaService.finalizarPartida(id, rpr);

                return ResponseEntity.ok().body(
                                ApiResponse.<PartidaResponse>builder()
                                                .success(true)
                                                .message("Partida finalizada con éxito")
                                                .data(partidaFinalizada)
                                                .build());
        }

        @Operation(summary = "Buscar Partida por ID", description = "Busca una partida específica por su ID")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Partida Encontrada"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Partida no encontrada")
        })
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<PartidaResponse>> buscarPartidaId(
                        @Parameter(description = "ID de la partida a buscar", example = "1", required = true) @PathVariable Long id) {
                log.info("GET /api/v1/partidas/{} - Buscar Partida por ID", id);

                PartidaResponse partidaEncontrada = partidaService.obtenerPorId(id);

                return ResponseEntity.ok().body(
                                ApiResponse.<PartidaResponse>builder()
                                                .success(true)
                                                .message("Partida Encontrada")
                                                .data(partidaEncontrada)
                                                .build());
        }

        @Operation(summary = "Listar todas las Partidas", description = "Muestra todas las partidas registradas en el sistema")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Partidas Encontradas")
        })
        @GetMapping
        @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
        public ResponseEntity<ApiResponse<List<PartidaResponse>>> mostrarTodasLasPartidas() {
                log.info("GET /api/v1/partidas - Mostrando todas las Partidas");

                List<PartidaResponse> partidasEncontradas = partidaService.listarTodas();

                return ResponseEntity.ok().body(
                                ApiResponse.<List<PartidaResponse>>builder()
                                                .success(true)
                                                .message("Partidas Encontradas")
                                                .data(partidasEncontradas)
                                                .build());
        }
}