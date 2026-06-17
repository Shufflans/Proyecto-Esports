package com.esports.ms_transferencias.controller;

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

import com.esports.ms_transferencias.dto.ApiResponse;
import com.esports.ms_transferencias.dto.TransferenciaRequest;
import com.esports.ms_transferencias.dto.TransferenciaResponse;
import com.esports.ms_transferencias.service.TransferenciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Transferencias", description = "Operaciones relacionadas con las Transferencias de jugadores")
@RestController
@RequestMapping("/api/v1/transferencias")
@RequiredArgsConstructor
@Slf4j
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    @Operation(summary = "Registrar Transferencia", description = "Registra un nuevo movimiento, fichaje o baja de un jugador conectándose con otros microservicios")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Transferencia registrada con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos o tipo de transferencia inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El jugador o el equipo ingresado no existe")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransferenciaResponse>> registrarTransferencia(
            @Valid @RequestBody TransferenciaRequest tr, @RequestHeader("Authorization") String token) {
        log.info("POST /api/v1/transferencias - Registrando Transferencia para jugador ID: {}", tr.getIdJugador());

        TransferenciaResponse transferenciaRecuperada = transferenciaService.registrarTransferencia(tr, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<TransferenciaResponse>builder()
                        .success(true)
                        .message("Transferencia Registrada con éxito")
                        .data(transferenciaRecuperada)
                        .build());
    }

    @Operation(summary = "Listar todas las Transferencias", description = "Muestra el historial completo de todas las transferencias de la aplicación")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transferencias Encontradas")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<TransferenciaResponse>>> mostrarTodasLasTransferencias() {
        log.info("GET /api/v1/transferencias - Mostrando todas las Transferencias");

        List<TransferenciaResponse> transferenciasEncontradas = transferenciaService.listarTodasLasTransferencias();

        return ResponseEntity.ok().body(
                ApiResponse.<List<TransferenciaResponse>>builder()
                        .success(true)
                        .message("Transferencias Listadas con éxito")
                        .data(transferenciasEncontradas)
                        .build());
    }

    @Operation(summary = "Listar Transferencias por Jugador", description = "Muestra todo el historial de transferencias asociado a un ID de jugador específico")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transferencias Encontradas")
    })
    @GetMapping("/jugador/{id}")
    public ResponseEntity<ApiResponse<List<TransferenciaResponse>>> mostrarTransferenciasPorJugador(
            @Parameter(description = "ID del jugador para filtrar su historial", example = "1", required = true) @PathVariable Long id) {
        log.info("GET /api/v1/transferencias/jugador/{} - Mostrando Transferencias por ID Jugador", id);

        List<TransferenciaResponse> transferenciasEncontradas = transferenciaService.buscarTransferenciasPorJugador(id);

        return ResponseEntity.ok().body(
                ApiResponse.<List<TransferenciaResponse>>builder()
                        .success(true)
                        .message("Transferencias por Jugador listadas")
                        .data(transferenciasEncontradas)
                        .build());
    }

    @Operation(summary = "Listar Transferencias por Tipo", description = "Muestra todas las transferencias que correspondan al tipo ingresado (ej. PRESTAMO, FICHAJE_INICIAL, BAJA)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transferencias Encontradas")
    })
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ApiResponse<List<TransferenciaResponse>>> mostrarTransferenciasPorTipo(
            @Parameter(description = "Tipo de transferencia a buscar", example = "PRESTAMO", required = true) @PathVariable String tipo) {
        log.info("GET /api/v1/transferencias/tipo/{} - Mostrando Transferencias por Tipo", tipo);

        List<TransferenciaResponse> transferenciasEncontradas = transferenciaService.buscarTransferenciasPorTipo(tipo);

        return ResponseEntity.ok().body(
                ApiResponse.<List<TransferenciaResponse>>builder()
                        .success(true)
                        .message("Transferencias por Tipo listadas")
                        .data(transferenciasEncontradas)
                        .build());
    }
}