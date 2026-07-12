package com.esports.ms_auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esports.ms_auth.dto.ApiResponse;
import com.esports.ms_auth.dto.AuthResponse;
import com.esports.ms_auth.dto.LoginRequest;
import com.esports.ms_auth.dto.RefreshTokenRequest;
import com.esports.ms_auth.dto.RegisterRequest;
import com.esports.ms_auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticación", description = "Operaciones de registro, login y renovación de Token.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

        private final AuthService authService;

        @Operation(summary = "Registro de Usuario", description = "Registra al usuario entregandole un Token")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario registrado con éxito"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas")
        })
        @PostMapping("/register")
        public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
                log.info("POST /auth/register - usuario: {}", registerRequest.getUsername());

                AuthResponse respuesta = authService.register(registerRequest);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.<AuthResponse>builder()
                                                .success(true)
                                                .message("Usuario registrado exitosamente")
                                                .data(respuesta)
                                                .build());
        }

        @Operation(summary = "Iniciar sesión", description = "Valida credenciales y retorna accessToken y refreshToken")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas")
        })
        @PostMapping("/login")
        public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
                log.info("POST /auth/login - usuario: {}", loginRequest.getUsername());

                AuthResponse respuesta = authService.login(loginRequest);

                return ResponseEntity.ok(
                                ApiResponse.<AuthResponse>builder()
                                                .success(true)
                                                .message("El usuario se ha logeado con éxito")
                                                .data(respuesta)
                                                .build());
        }

        @Operation(summary = "Refrescar Token", description = "Renueva el accestoken caducado")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token Refrescado con éxito"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas")
        })
        @PostMapping("/refresh")
        public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {

                AuthResponse respuesta = authService.refresh(refreshTokenRequest.getRefreshToken());

                return ResponseEntity.ok(
                                ApiResponse.<AuthResponse>builder()
                                                .success(true)
                                                .message("Token renovado")
                                                .data(respuesta)
                                                .build());
        }
}
