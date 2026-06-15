package com.esports.ms_equipos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esports.ms_equipos.dto.ApiResponse;
import com.esports.ms_equipos.dto.EquipoRequest;
import com.esports.ms_equipos.dto.EquipoResponse;
import com.esports.ms_equipos.dto.RosterHistoricoRequest;
import com.esports.ms_equipos.dto.StaffTecnicoRequest;
import com.esports.ms_equipos.service.EquipoService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Equipos", description = "Operaciones relacionadas con los equipos")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/equipos")
public class EquipoController {

    private final EquipoService equipoService;

    @Operation(summary = "Crear Equipos", description = "Crear los equipos")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Equipo creado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El Equipo ya existe")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<EquipoResponse>> crearEquipo(@Valid @RequestBody EquipoRequest er) {
        log.info("POST /api/v1/equipos - Equipo: {}", er.getNombreEquipo());

        EquipoResponse creado = equipoService.crearEquipo(er);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<EquipoResponse>builder()
                        .success(true)
                        .message("El Equipo ha sido creado con éxito")
                        .data(creado)
                        .build());
    }

    @Operation(summary = "Agregar Staff al Equipo", description = "Agrega un staff al Equipo")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Staff agregado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El Equipo ya existe")
    })
    @PostMapping("/{id}/staff")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<EquipoResponse>> agregarStaff(@PathVariable Long id,
            @Valid @RequestBody StaffTecnicoRequest st) {
        log.info("PUT /api/v1/equipos/{}/staff - Staff: {}", id, st.getNombreStaff());

        EquipoResponse staffAgregado = equipoService.agregarStaff(id, st);

        return ResponseEntity.ok().body(
                ApiResponse.<EquipoResponse>builder()
                        .success(true)
                        .message("Staff Agregado Correctamente")
                        .data(staffAgregado)
                        .build());
    }

    @Operation(summary = "Agregar Jugador", description = "Agrega un jugador al Roster del Equipo")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Jugador agregado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El Equipo ya existe"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @PostMapping("/{id}/roster")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<EquipoResponse>> agregarJugador(@PathVariable Long id,
            @Valid @RequestBody RosterHistoricoRequest rh,
            @RequestHeader("Authorization") String token) {
        log.info("PUT /api/v1/equipos/{}/roster - Jugador: {}", id, rh.getIdJugador());

        EquipoResponse jugadorAgregado = equipoService.agregarJugador(id, rh, token);

        return ResponseEntity.ok().body(
                ApiResponse.<EquipoResponse>builder()
                        .success(true)
                        .message("Jugador agregado al Roster")
                        .data(jugadorAgregado)
                        .build());
    }

    @Operation(summary = "Listar equipos", description = "Muestra todos los equipos agregados")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Equipos listados")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<EquipoResponse>>> mostrarEquipos() {
        log.info("GET /api/v1/equipos - Mostrando Equipos");

        List<EquipoResponse> mostrandoEquipos = equipoService.listarEquipos();

        return ResponseEntity.ok().body(
                ApiResponse.<List<EquipoResponse>>builder()
                        .success(true)
                        .message("Lista mostrada con éxito")
                        .data(mostrandoEquipos)
                        .build());
    }

    @Operation(summary = "Listar equipos por región", description = "Muestra todos los equipos por región")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Equipos listados por región")
    })
    @GetMapping("/region/{region}")
    public ResponseEntity<ApiResponse<List<EquipoResponse>>> mostrarEquiposPorRegion(@PathVariable String region) {
        log.info("GET /api/v1/equipos/region/{} - Mostrando Equipos por Region", region);

        List<EquipoResponse> mostrandoEquipos = equipoService.listarEquiposPorRegion(region);

        return ResponseEntity.ok().body(
                ApiResponse.<List<EquipoResponse>>builder()
                        .success(true)
                        .message("Lista de equipos por región")
                        .data(mostrandoEquipos)
                        .build());
    }

    @Operation(summary = "Actualizar equipo", description = "Actualiza equipo con nuevos datos")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Equipo actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos erróneos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<EquipoResponse>> actualizarEquipo(@PathVariable Long id,
            @Valid @RequestBody EquipoRequest er) {
        log.info("PUT /api/v1/equipos/{} - Actualizando Equipo: {}", id, er.getNombreEquipo());

        EquipoResponse equipoActualizado = equipoService.actualizarEquipo(id, er);

        return ResponseEntity.ok().body(
                ApiResponse.<EquipoResponse>builder()
                        .success(true)
                        .message("Equipo Actualizado con éxito")
                        .data(equipoActualizado)
                        .build());
    }

    @Operation(summary = "Desactivar Equipo", description = "Desactiva al equipo")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Equipo Desactivado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> desactivarEquipo(@PathVariable Long id) {
        log.info("DELETE /api/v1/equipos/{} - Desactivando equipo", id);

        equipoService.desactivarEquipo(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Equipo desactivado con éxito")
                        .build());
    }

    @Operation(summary = "Terminar Contrato", description = "Le pone una fecha de Fin al contrato del Jugador")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Contrato terminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Roster no encontrado")
    })
    @DeleteMapping("/roster/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> terminarContrato(@PathVariable Long id) {
        log.info("DELETE /api/v1/equipos/roster/{} - Terminando contrato", id);

        equipoService.terminarContrato(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Jugador despedido")
                        .build());
    }

    @Operation(summary = "Despedir Staff", description = "Despide al staff dejandolo inactivo")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Staff despedido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Staff no encontrado")
    })
    @DeleteMapping("/staff/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> despedirStaff(@PathVariable Long id) {
        log.info("DELETE /api/v1/equipos/staff/{} - Despidiendo Staff", id);

        equipoService.despedirStaff(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Staff Despedido")
                        .build());
    }

}
