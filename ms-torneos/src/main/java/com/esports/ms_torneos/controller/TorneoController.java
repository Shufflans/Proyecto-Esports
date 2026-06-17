package com.esports.ms_torneos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esports.ms_torneos.dto.ApiResponse;
import com.esports.ms_torneos.dto.TorneoRequest;
import com.esports.ms_torneos.dto.TorneoResponse;
import com.esports.ms_torneos.service.TorneoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Torneos", description = "Operaciones relacionadas con los Torneos")
@RestController
@RequestMapping("/api/v1/torneos")
@RequiredArgsConstructor
@Slf4j
public class TorneoController {

    private final TorneoService torneoService;

    @Operation(summary = "Crear Torneo", description = "Crea un nuevo torneo dandole un id de un juego existente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Torneo creado con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos o Juego desactivado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El juego ingresado no existe")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TorneoResponse>> crearTorneo(
            @Valid @RequestBody TorneoRequest tr, @RequestHeader("Authorization") String token) {
        log.info("POST /api/v1/torneos - Creando Torneo: {}", tr.getNombreTorneo());

        TorneoResponse torneoRecuperado = torneoService.crearTorneo(tr, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<TorneoResponse>builder()
                        .success(true)
                        .message("Torneo Creado con éxito")
                        .data(torneoRecuperado)
                        .build());
    }

    @Operation(summary = "Buscar Torneo por ID", description = "Busca un torneo específico por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Torneo Encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TorneoResponse>> buscarTorneoId(
            @Parameter(description = "ID del Torneo a buscar", example = "1", required = true) @PathVariable Long id) {
        log.info("GET /api/v1/torneos/{} - Buscar Torneo por ID", id);

        TorneoResponse torneoEncontrado = torneoService.buscarPorId(id);

        return ResponseEntity.ok().body(
                ApiResponse.<TorneoResponse>builder()
                        .success(true)
                        .message("Torneo Encontrado")
                        .data(torneoEncontrado)
                        .build());
    }

    @Operation(summary = "Listar todos los Torneos", description = "Muestra todos los torneos de la aplicación")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Torneos Encontrados")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<TorneoResponse>>> mostrarTodosLosTorneos() {
        log.info("GET /api/v1/torneos - Mostrando todos los Torneos");

        List<TorneoResponse> torneosEncontrados = torneoService.listarTodosLosTorneos();

        return ResponseEntity.ok().body(
                ApiResponse.<List<TorneoResponse>>builder()
                        .success(true)
                        .message("Torneos Listados con éxito")
                        .data(torneosEncontrados)
                        .build());
    }

    @Operation(summary = "Listar Torneos por Estado", description = "Muestra todos los torneos que tengan el estado ingresado (ej. PROGRAMADO, FINALIZADO)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Torneos Encontrados")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<TorneoResponse>>> mostrarTorneosPorEstado(
            @Parameter(description = "Estado del torneo a buscar", example = "PROGRAMADO", required = true) @PathVariable String estado) {
        log.info("GET /api/v1/torneos/estado/{} - Mostrando Torneos por Estado", estado);

        List<TorneoResponse> torneosEncontrados = torneoService.listarTorneosPorEstado(estado);

        return ResponseEntity.ok().body(
                ApiResponse.<List<TorneoResponse>>builder()
                        .success(true)
                        .message("Torneos por Estado listados")
                        .data(torneosEncontrados)
                        .build());
    }

    @Operation(summary = "Listar Torneos por Juego", description = "Muestra todos los torneos asociados a un ID de juego específico")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Torneos Encontrados")
    })
    @GetMapping("/juego/{id}")
    public ResponseEntity<ApiResponse<List<TorneoResponse>>> mostrarTorneosPorIdJuego(
            @Parameter(description = "ID del juego para filtrar torneos", example = "1", required = true) @PathVariable Long id) {
        log.info("GET /api/v1/torneos/juego/{} - Mostrando Torneos por ID Juego", id);

        List<TorneoResponse> torneosEncontrados = torneoService.listarTorneosPorIdJuego(id);

        return ResponseEntity.ok().body(
                ApiResponse.<List<TorneoResponse>>builder()
                        .success(true)
                        .message("Torneos por Juego listados")
                        .data(torneosEncontrados)
                        .build());
    }

    @Operation(summary = "Actualizar Torneo", description = "Modificar los datos permitidos de un torneo ya ingresado")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Torneo actualizado con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El torneo no existe"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El torneo ya está finalizado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TorneoResponse>> actualizarTorneo(
            @Parameter(description = "ID del torneo que se desea modificar", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody TorneoRequest tr) {
        log.info("PUT /api/v1/torneos/{} - Actualizando Torneo: {}", id, tr.getNombreTorneo());

        TorneoResponse torneoRecuperado = torneoService.actualizarTorneo(id, tr);

        return ResponseEntity.ok().body(
                ApiResponse.<TorneoResponse>builder()
                        .success(true)
                        .message("Torneo actualizado con éxito")
                        .data(torneoRecuperado)
                        .build());
    }

    @Operation(summary = "Desactivar/Finalizar Torneo", description = "Cambia el estado del torneo a TERMINADO")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Torneo desactivado con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El torneo no existe")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> desactivarTorneo(
            @Parameter(description = "ID del torneo que se desea desactivar", example = "1", required = true) @PathVariable Long id) {
        log.info("DELETE /api/v1/torneos/{} - Desactivando Torneo", id);

        torneoService.desactivarTorneo(id);

        return ResponseEntity.noContent().build();
    }
}