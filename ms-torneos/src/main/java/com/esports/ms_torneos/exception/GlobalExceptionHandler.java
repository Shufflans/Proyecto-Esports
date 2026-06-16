package com.esports.ms_torneos.exception;

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

    @ExceptionHandler(JuegoDesactivadoException.class)
    public ResponseEntity<ErrorResponse> handleJuegoDesactivado(JuegoDesactivadoException ex, HttpServletRequest rq) {
        log.warn("Error al buscar Juego: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Juego Desactivado")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(NoExisteJuegoConIdException.class)
    public ResponseEntity<ErrorResponse> handleNoExisteJuegoId(NoExisteJuegoConIdException ex,
            HttpServletRequest rq) {
        log.warn("No Existe Juego: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("No existe el juego buscado")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TorneoFinalizadoException.class)
    public ResponseEntity<ErrorResponse> handleTorneoFinalizado(TorneoFinalizadoException ex, HttpServletRequest rq) {
        log.warn("Torneo ya Finalizado: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("El Torneo está desactivado")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(TorneoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleTorneoNoEncontrado(TorneoNoEncontradoException ex,
            HttpServletRequest rq) {
        log.warn("Torneo no fue encontrado: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("El Torneo no exsite")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(MethodArgumentNotValidException ex, HttpServletRequest rq) {

        Map<String, String> errores = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errores.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ;

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
