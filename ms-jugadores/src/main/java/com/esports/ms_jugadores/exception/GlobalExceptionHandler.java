package com.esports.ms_jugadores.exception;

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

    @ExceptionHandler(EdadMinimaException.class)
    public ResponseEntity<ErrorResponse> handleEdadMinima(EdadMinimaException ex, HttpServletRequest rq) {
        log.warn("Validación de edad fallida: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validación de edad fallida")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(JugadorNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleJugadorNoEncontrado(JugadorNoEncontradoException ex,
            HttpServletRequest rq) {
        log.warn("Validación de busqueda fallido: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("No existe el recurso buscado")
                .mensaje(ex.getMessage())
                .path(rq.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NickNameDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleNickNameDuplicado(NickNameDuplicadoException ex, HttpServletRequest rq) {
        log.warn("Validación de nombre fallida: {} | URI: {}", ex.getMessage(), rq.getRequestURI());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("El Nombre está repetido")
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
