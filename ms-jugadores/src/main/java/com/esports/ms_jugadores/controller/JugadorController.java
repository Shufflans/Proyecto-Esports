package com.esports.ms_jugadores.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esports.ms_jugadores.dto.ApiResponse;
import com.esports.ms_jugadores.dto.JugadorRequest;
import com.esports.ms_jugadores.dto.JugadorResponse;
import com.esports.ms_jugadores.service.JugadorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/jugadores")
@RequiredArgsConstructor
@Slf4j
public class JugadorController {
    private final JugadorService jugadorService;

    @PostMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JugadorResponse>> buscarJugadoresId(@PathVariable Long id) {
        log.info("GET /api/v1/jugadores - ID: {}", id);

        JugadorResponse encontrado = jugadorService.buscarPorId(id);

        return ResponseEntity.ok(
                ApiResponse.<JugadorResponse>builder()
                        .success(true)
                        .message("Jugador encontrado")
                        .data(encontrado)
                        .build());
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<ApiResponse<List<JugadorResponse>>> mostrarPorPais(@PathVariable String pais) {
        log.info("GET /api/v1/jugadores - pais: {}", pais);

        List<JugadorResponse> jugadoresEncontrados = jugadorService.mostrarPorPaises(pais);

        return ResponseEntity.ok(
                ApiResponse.<List<JugadorResponse>>builder()
                        .success(true)
                        .message("Jugador encontrado")
                        .data(jugadoresEncontrados)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JugadorResponse>> actualizarJugador(@PathVariable Long id,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desactivarJugador(@PathVariable Long id) {
        log.info("DELETE /api/v1/jugadores - Eliminado jugador con ID: {}", id);

        jugadorService.desactivarJugador(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Jugador desactivado")
                        .build());
    }

}
