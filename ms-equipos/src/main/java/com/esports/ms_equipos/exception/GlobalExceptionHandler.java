package com.esports.ms_equipos.exception;

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

    @ExceptionHandler(EquipoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleEquipoNoEncontrado(EquipoNoEncontradoException ex,
            HttpServletRequest rq) {
        log.warn("El Equipo no ha sido encontrado: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("El equipo no se encontró")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(StaffNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleStaffNoEncontrado(StaffNoEncontradoException ex, HttpServletRequest rq) {
        log.warn("El Staff no ha sido encontrado: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("El staff no se encontró")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RosterIdNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRosterNoEncontrado(RosterIdNoEncontradoException ex,
            HttpServletRequest rq) {
        log.warn("El Roster no ha sido encontrado: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("El rosterno se encontró")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(JugadorNoExisteException.class)
    public ResponseEntity<ErrorResponse> handleJugadorNoExiste(JugadorNoExisteException ex, HttpServletRequest rq) {
        log.warn("El Jugador que buscado no existe: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("El Jugador no existe")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NombreYRegionYaExistenException.class)
    public ResponseEntity<ErrorResponse> handleNombreYRegionExistentes(NombreYRegionYaExistenException ex,
            HttpServletRequest rq) {
        log.warn("El Nombre y la Región ya existen: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("El Nombre y Region ya existen")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(YaExisteElHeadCoachException.class)
    public ResponseEntity<ErrorResponse> handleStaffExiste(YaExisteElHeadCoachException ex, HttpServletRequest rq) {
        log.warn("El rol del Staff ya existe: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("El Rol del Staff ya existe")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
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