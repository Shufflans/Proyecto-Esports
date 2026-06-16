package com.esports.ms_juegos.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esports.ms_juegos.dto.ApiResponse;
import com.esports.ms_juegos.dto.JuegoRequest;
import com.esports.ms_juegos.dto.JuegoResponse;
import com.esports.ms_juegos.service.JuegoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Juegos", description = "Operaciones relacionadas con la creación o actualización de Juegos")
@RestController
@RequestMapping("/api/v1/juegos")
@RequiredArgsConstructor
@Slf4j
public class JuegoController {
        private final JuegoService juegoService;

        @Operation(summary = "Crear Juego", description = "Crea un juego, el nombre debe ser único")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Juego creado con éxito"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El jugador ya existe")
        })
        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<JuegoResponse>> crearJuego(@Valid @RequestBody JuegoRequest jr) {
                log.info("POST /api/v1/juegos - Creando Juego: {}", jr.getNombreJuego());

                JuegoResponse juegoRecuperado = juegoService.crearJuego(jr);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.<JuegoResponse>builder()
                                                .success(true)
                                                .message("Juego Creado con éxito")
                                                .data(juegoRecuperado)
                                                .build());
        }

        @Operation(summary = "Buscar Juego ID", description = "Busca un juego específico por su ID")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Juego Encontrado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Juego no encontrado")
        })
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<JuegoResponse>> buscarJuegoId(
                        @Parameter(description = "ID Del Juego a buscar", example = "1", required = true) @PathVariable Long id) {
                log.info("GET /api/v1/juegos/{} - Buscar Juego por ID", id);

                JuegoResponse juegoEncontrado = juegoService.buscarJuegoPorId(id);

                return ResponseEntity.ok().body(
                                ApiResponse.<JuegoResponse>builder()
                                                .success(true)
                                                .message("Juego Encontrado")
                                                .data(juegoEncontrado)
                                                .build());
        }

        @Operation(summary = "Listar todos los Juegos", description = "Muestra todos los juegos dentro de la aplicación")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Juegos Encontrados"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Juegos no encontrados")
        })
        @GetMapping
        @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
        public ResponseEntity<ApiResponse<List<JuegoResponse>>> mostrarTodosLosJuegos() {
                log.info("GET /api/v1/juegos - Mostrando los Juegos");

                List<JuegoResponse> juegosEncontrados = juegoService.listarTodosLosJuegos();

                return ResponseEntity.ok().body(
                                ApiResponse.<List<JuegoResponse>>builder()
                                                .success(true)
                                                .message("Juego Encontrado")
                                                .data(juegosEncontrados)
                                                .build());
        }

        @Operation(summary = "Listar Juegos Activos", description = "Muestra todos los juegos que estén activos en la aplicación")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Juegos Encontrados"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Juegos no encontrados")
        })
        @GetMapping("/activos")
        public ResponseEntity<ApiResponse<List<JuegoResponse>>> mostrarJuegosActivos() {
                log.info("GET /api/v1/juegos/activos - Mostrando Juegos Activos");

                List<JuegoResponse> juegosEncontrados = juegoService.listarJuegosActivos();

                return ResponseEntity.ok().body(
                                ApiResponse.<List<JuegoResponse>>builder()
                                                .success(true)
                                                .message("Juegos Activos Encontrados")
                                                .data(juegosEncontrados)
                                                .build());
        }

        @Operation(summary = "Listar Juegos por Genero", description = "Muestra todos los juegos que pertenezcan al genero ingresado")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Juegos Encontrados"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Juegos no encontrados")
        })
        @GetMapping("/genero/{genero}")
        public ResponseEntity<ApiResponse<List<JuegoResponse>>> mostrarJuegosPorGenero(
                        @Parameter(description = "Se debe ingresar el genero que se desea buscar", example = "MOBA", required = true) @PathVariable String genero) {
                log.info("GET /api/v1/juegos/genero/{} - Mostrando Juegos por Genero", genero);

                List<JuegoResponse> juegosEncontrados = juegoService.buscarJuegoPorGenero(genero);

                return ResponseEntity.ok().body(
                                ApiResponse.<List<JuegoResponse>>builder()
                                                .success(true)
                                                .message("Juegos por Generos listados")
                                                .data(juegosEncontrados)
                                                .build());
        }

        @Operation(summary = "Actualizar Juego", description = "Modificar los datos de un juego ya ingresado")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Juego actualizado con éxito"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El juego no existe")
        })
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<JuegoResponse>> actualizarJuego(
                        @Parameter(description = "Se debe ingresar el ID del juego que se desea modificar", example = "1", required = true) @PathVariable Long id,
                        @Valid @RequestBody JuegoRequest jr) {
                log.info("PUT /api/v1/juegos/{} - Actualizando Juego: {}", id, jr.getNombreJuego());

                JuegoResponse juegoRecuperado = juegoService.actualizarJuego(id, jr);

                return ResponseEntity.ok().body(
                                ApiResponse.<JuegoResponse>builder()
                                                .success(true)
                                                .message("Juego actualizado con éxito")
                                                .data(juegoRecuperado)
                                                .build());
        }

        @Operation(summary = "Desactivar Juego", description = "Desactivar el juego dentro de la aplicación")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Juego desactivado con éxito"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El juego no existe")
        })
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> desactivarJuego(
                        @Parameter(description = "Se debe ingrear el ID del juego que se desea desactivar", example = "1", required = true) @PathVariable Long id) {
                log.info("DELETE /api/v1/juegos/{} - Desactivando Juego", id);

                juegoService.desactivarJuego(id);

                return ResponseEntity.noContent().build();
        }

}