package com.esports.ms_auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.esports.ms_auth.dto.AuthResponse;
import com.esports.ms_auth.dto.RegisterRequest;
import com.esports.ms_auth.model.Usuario;
import com.esports.ms_auth.repository.RefreshTokenRepository;
import com.esports.ms_auth.repository.UsuarioRepository;

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

        user.setUsuario(registerRequest.getUsername());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setRole("ROLE_USER");

        usuarioRepository.save(user);
        String acceso = jwtUtil.generarToken(user.getUsuario(), user.getRole());
        String refresh = generarRefreshToken(user.getUsuario());

        return new AuthResponse(acceso, refresh);
    }
}
