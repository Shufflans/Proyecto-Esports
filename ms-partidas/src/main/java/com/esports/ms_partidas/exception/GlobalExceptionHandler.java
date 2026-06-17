package com.esports.ms_partidas.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PartidaNoExisteException.class)
    public ResponseEntity<ErrorResponse> handlePartidaNoExiste(PartidaNoExisteException ex, HttpServletRequest rq) {
        log.warn("Partida no encontrada: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("No existe la partida buscada")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TorneoNoExisteException.class)
    public ResponseEntity<ErrorResponse> handleTorneoNoExiste(TorneoNoExisteException ex, HttpServletRequest rq) {
        log.warn("Torneo no encontrado: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("No existe el torneo")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EquiposNoExistenException.class)
    public ResponseEntity<ErrorResponse> handleEquiposNoExisten(EquiposNoExistenException ex, HttpServletRequest rq) {
        log.warn("Equipo no encontrado: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("No existen los equipos")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MismoEquipoException.class)
    public ResponseEntity<ErrorResponse> handleMismoEquipo(MismoEquipoException ex, HttpServletRequest rq) {
        log.warn("Mismo equipo en partida: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Conflicto de equipos")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EquiposDesactivadosException.class)
    public ResponseEntity<ErrorResponse> handleEquiposDesactivados(EquiposDesactivadosException ex,
            HttpServletRequest rq) {
        log.warn("Equipos inactivos: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Equipos desactivados")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PartidaYaFinalizadaException.class)
    public ResponseEntity<ErrorResponse> handlePartidaYaFinalizada(PartidaYaFinalizadaException ex,
            HttpServletRequest rq) {
        log.warn("Conflicto de estado de partida: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Partida ya finalizada")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(GanadorInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleGanadorInvalido(GanadorInvalidoException ex, HttpServletRequest rq) {
        log.warn("Equipo ganador inválido: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Ganador no corresponde")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(MethodArgumentNotValidException ex, HttpServletRequest rq) {

        Map<String, String> errores = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errores.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.warn("Validacion fallida: {} | URI: {}", errores, rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validación fallida")
                .mensaje("Falló la validación de uno o más campos")
                .path(rq.getRequestURI())
                .erroresValidacion(errores)
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccesoDenegado(AccessDeniedException ex, HttpServletRequest rq) {
        log.warn("Usuario no autorizado: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Autorización denegada")
                .mensaje("Usuario no autorizado")
                .path(rq.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenerico(Exception ex, HttpServletRequest rq) {
        log.error("Error no controlado: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Error interno del servidor")
                .mensaje("Contactar al administrador")
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.internalServerError().body(error);
    }
}