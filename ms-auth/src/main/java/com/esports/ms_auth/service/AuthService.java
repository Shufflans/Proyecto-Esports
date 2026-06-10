package com.esports.ms_auth.service;

import java.sql.Date;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.esports.ms_auth.dto.AuthResponse;
import com.esports.ms_auth.dto.LoginRequest;
import com.esports.ms_auth.dto.RegisterRequest;
import com.esports.ms_auth.model.RefreshToken;
import com.esports.ms_auth.model.Usuario;
import com.esports.ms_auth.repository.RefreshTokenRepository;
import com.esports.ms_auth.repository.UsuarioRepository;
import com.esports.ms_auth.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenRepository refreshRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest registerRequest) {
        Usuario user = new Usuario();

        user.setUsername(registerRequest.getUsername());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setRole("ROLE_USER");

        usuarioRepository.save(user);
        String acceso = jwtUtil.generarToken(user.getUsername(), user.getRole());
        String refresh = generarRefreshToken(user.getUsername());

        return new AuthResponse(acceso, refresh);
    }

    public AuthResponse login(LoginRequest loginRequest) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        Usuario user = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario inválido"));

        String acceso = jwtUtil.generarToken(user.getUsername(), user.getRole());
        String refresh = generarRefreshToken(user.getUsername());

        return new AuthResponse(acceso, refresh);
    }

    public AuthResponse refresh(String refreshToken) {

        RefreshToken token = refreshRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh inválido"));

        if (!jwtUtil.esValido(refreshToken) || !jwtUtil.esRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh token inválido");
        }

        Usuario user = usuarioRepository.findByUsername(token.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String nuevoAcceso = jwtUtil.generarToken(user.getUsername(), user.getRole());

        return new AuthResponse(nuevoAcceso, refreshToken);
    }

    private String generarRefreshToken(String username) {

        String token = UUID.randomUUID().toString();

        RefreshToken rt = new RefreshToken();

        rt.setToken(token);
        rt.setUsername(username);
        rt.setExpiryDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));

        refreshRepository.save(rt);

        return token;

    }
}
