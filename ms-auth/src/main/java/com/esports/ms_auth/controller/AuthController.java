package com.esports.ms_auth.controller;

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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("POST /auth/register - usuario: {}", registerRequest.getUsername());

        AuthResponse respuesta = authService.register(registerRequest);

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Usuario registrado exitosamente")
                        .data(respuesta)
                        .build());
    }

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
