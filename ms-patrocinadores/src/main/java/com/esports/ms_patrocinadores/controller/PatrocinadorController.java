package com.esports.ms_patrocinadores.controller;

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

import com.esports.ms_patrocinadores.dto.ApiResponse;
import com.esports.ms_patrocinadores.dto.PatrocinadorRequest;
import com.esports.ms_patrocinadores.dto.PatrocinadorResponse;
import com.esports.ms_patrocinadores.service.PatrocinadorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Patrocinadores", description = "Operaciones relacionadas con los Patrocinadores de equipos")
@RestController
@RequestMapping("/api/v1/patrocinadores")
@RequiredArgsConstructor
@Slf4j
public class PatrocinadorController {

    private final PatrocinadorService patrocinadorService;

    @Operation(summary = "Registrar Patrocinador", description = "Registra un nuevo patrocinador conectándose con el microservicio de equipos")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Patrocinador registrado con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos de registro"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El equipo ingresado no existe")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PatrocinadorResponse>> registrarPatrocinador(
            @Valid @RequestBody PatrocinadorRequest pr, @RequestHeader("Authorization") String token) {
        log.info("POST /api/v1/patrocinadores - Registrando Patrocinador para equipo ID: {}", pr.getIdEquipo());

        PatrocinadorResponse patrocinadorRecuperado = patrocinadorService.registrarPatrocinador(pr, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<PatrocinadorResponse>builder()
                        .success(true)
                        .message("Patrocinador Registrado con éxito")
                        .data(patrocinadorRecuperado)
                        .build());
    }

    @Operation(summary = "Listar todos los Patrocinadores", description = "Muestra el registro completo de todos los patrocinadores de la aplicación")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patrocinadores Encontrados")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<PatrocinadorResponse>>> mostrarTodosLosPatrocinadores() {
        log.info("GET /api/v1/patrocinadores - Mostrando todos los Patrocinadores");

        List<PatrocinadorResponse> patrocinadoresEncontrados = patrocinadorService.listarTodosLosPatrocinadores();

        return ResponseEntity.ok().body(
                ApiResponse.<List<PatrocinadorResponse>>builder()
                        .success(true)
                        .message("Patrocinadores Listados con éxito")
                        .data(patrocinadoresEncontrados)
                        .build());
    }

    @Operation(summary = "Listar Patrocinadores por Equipo", description = "Muestra todos los patrocinadores asociados a un ID de equipo específico")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patrocinadores Encontrados")
    })
    @GetMapping("/equipo/{id}")
    public ResponseEntity<ApiResponse<List<PatrocinadorResponse>>> mostrarPatrocinadoresPorEquipo(
            @Parameter(description = "ID del equipo para filtrar sus patrocinadores", example = "1", required = true) @PathVariable Long id) {
        log.info("GET /api/v1/patrocinadores/equipo/{} - Mostrando Patrocinadores por ID Equipo", id);

        List<PatrocinadorResponse> patrocinadoresEncontrados = patrocinadorService.buscarPatrocinadoresPorEquipo(id);

        return ResponseEntity.ok().body(
                ApiResponse.<List<PatrocinadorResponse>>builder()
                        .success(true)
                        .message("Patrocinadores por Equipo listados")
                        .data(patrocinadoresEncontrados)
                        .build());
    }
}