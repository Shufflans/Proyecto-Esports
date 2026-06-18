package com.esports.ms_jugadores.controller;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.esports.ms_jugadores.dto.ApiResponse;
import com.esports.ms_jugadores.dto.JugadorRequest;
import com.esports.ms_jugadores.dto.JugadorResponse;
import com.esports.ms_jugadores.service.JugadorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Jugadores", description = "Operaciones relacionadas con jugadores.")
@RestController
@RequestMapping("/api/v1/jugadores")
@RequiredArgsConstructor
@Slf4j
public class JugadorController {
        private final JugadorService jugadorService;

        @Operation(summary = "Crear Jugadores", description = "Crea los jugadores.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Jugador creado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El jugador ya existe")
        })
        @PostMapping
        @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
        public ResponseEntity<ApiResponse<JugadorResponse>> crearJugador(@Valid @RequestBody JugadorRequest jr) {
                log.info("POST /api/v1/jugadores - nickcname: {}", jr.getNickname());

                JugadorResponse creado = jugadorService.crear(jr);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.<JugadorResponse>builder()
                                                .success(true)
                                                .message("Jugador creado con éxito")
                                                .data(creado)
                                                .build());
        }

        @Operation(summary = "Listar Jugadores", description = "Retorna un listado de todos los jugadores.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Devuelve correctamente los jugadores"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })
        @GetMapping
        public ResponseEntity<ApiResponse<List<JugadorResponse>>> listarJugadores() {
                log.info("GET /api/v1/jugadores - Lista");

                List<JugadorResponse> jugadoresLista = jugadorService.listarTodos();

                return ResponseEntity.ok(
                                ApiResponse.<List<JugadorResponse>>builder()
                                                .success(true)
                                                .message("Lista de jugadores exitosa")
                                                .data(jugadoresLista)
                                                .build());
        }

        @Operation(summary = "Buscar Jugadores Por ID", description = "Retorna al jugador buscado por su ID.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Devuelve al jugador encontrado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Jugador no encontrado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<EntityModel<JugadorResponse>>> buscarJugadoresId(
                        @Parameter(description = "Ingresa el ID del jugador que deseas buscar", example = "1") @PathVariable Long id) {
                log.info("GET /api/v1/jugadores - ID: {}", id);

                JugadorResponse encontrado = jugadorService.buscarPorId(id);
                EntityModel<JugadorResponse> recurso = EntityModel.of(encontrado);

                recurso.add(linkTo(methodOn(JugadorController.class).buscarJugadoresId(id)).withSelfRel());
                recurso.add(linkTo(methodOn(JugadorController.class).listarJugadores()).withRel("all"));
                recurso.add(linkTo(methodOn(JugadorController.class).actualizarJugador(id, null)).withRel("update"));
                recurso.add(linkTo(methodOn(JugadorController.class).desactivarJugador(id)).withRel("delete"));

                return ResponseEntity.ok(
                                ApiResponse.<EntityModel<JugadorResponse>>builder()
                                                .success(true)
                                                .message("Jugador encontrado")
                                                .data(recurso)
                                                .build());
        }

        @Operation(summary = "Buscar por País", description = "Retorna una lista de jugadores por País.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna jugadores del país ingresado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "País no encontrado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No hay jugadores de ese país"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })
        @GetMapping("/pais/{pais}")
        public ResponseEntity<ApiResponse<List<JugadorResponse>>> mostrarPorPais(
                        @Parameter(example = "Chile", description = "Se debe colocar el país del jugador que se desea buscar") @PathVariable String pais) {
                log.info("GET /api/v1/jugadores - pais: {}", pais);

                List<JugadorResponse> jugadoresEncontrados = jugadorService.mostrarPorPaises(pais);

                return ResponseEntity.ok(
                                ApiResponse.<List<JugadorResponse>>builder()
                                                .success(true)
                                                .message("Jugadores encontrados")
                                                .data(jugadoresEncontrados)
                                                .build());
        }

        @Operation(summary = "Actualizar Datos", description = "Actualiza los datos de un jugador y retorna al mismo jugador actualizado.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Se actualiza el jugador y se retorna al mismo"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Ya existe el nombre del jugador a actualizar"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })
        @PutMapping("/{id}")
        @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
        public ResponseEntity<ApiResponse<JugadorResponse>> actualizarJugador(
                        @Parameter(description = "ID del jugador a desactivar", example = "1") @PathVariable Long id,
                        @Valid @RequestBody JugadorRequest jr) {
                log.info("PUT /api/v1/jugadores - Actualizando jugador: {}", jr.getNickname());

                JugadorResponse jugadorActualizar = jugadorService.actualizar(id, jr);

                return ResponseEntity.ok(
                                ApiResponse.<JugadorResponse>builder()
                                                .success(true)
                                                .message("Jugador actualizado")
                                                .data(jugadorActualizar)
                                                .build());
        }

        @Operation(summary = "Desactivar Jugador", description = "Desactiva al jugador por su ID.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Se desactiva al jugador pero no se devuelve nada"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No existe el ID del usuario a desactivas"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> desactivarJugador(
                        @Parameter(description = "ID del jugador a desactivar", example = "1") @PathVariable Long id) {
                log.info("DELETE /api/v1/jugadores - Eliminado jugador con ID: {}", id);

                jugadorService.desactivarJugador(id);

                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                                ApiResponse.<Void>builder()
                                                .success(true)
                                                .message("Jugador desactivado")
                                                .build());
        }

}
