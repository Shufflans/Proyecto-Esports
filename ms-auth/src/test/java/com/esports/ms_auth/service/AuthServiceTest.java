package com.esports.ms_auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.esports.ms_auth.dto.AuthResponse;
import com.esports.ms_auth.dto.LoginRequest;
import com.esports.ms_auth.dto.RegisterRequest;
import com.esports.ms_auth.model.RefreshToken;
import com.esports.ms_auth.model.Usuario;
import com.esports.ms_auth.repository.RefreshTokenRepository;
import com.esports.ms_auth.repository.UsuarioRepository;
import com.esports.ms_auth.security.JwtUtil;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private RefreshTokenRepository refreshRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService service;

    @Test
    void deberiaRegistrarUsuarioCorrectamente() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("camilo123");
        registerRequest.setPassword("claveSegura1");

        when(encoder.encode("claveSegura1")).thenReturn("claveEncriptada");
        when(jwtUtil.generarToken("camilo123", "ROLE_USER")).thenReturn("token");

        AuthResponse resultado = service.register(registerRequest);

        assertNotNull(resultado);
        assertEquals("token", resultado.getAccessToken());
        assertNotNull(resultado.getRefreshToken());
        verify(usuarioRepository).save(any(Usuario.class));
        verify(refreshRepository).save(any(RefreshToken.class));
    }

    @Test
    void deberiaAsignarRoleUserAlRegistrar() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("nuevoUsuario");
        registerRequest.setPassword("password123");

        when(encoder.encode("password123")).thenReturn("hashGenerado");
        when(jwtUtil.generarToken("nuevoUsuario", "ROLE_USER")).thenReturn("token123");

        service.register(registerRequest);

        verify(usuarioRepository).save(argThat(usuario -> usuario.getRole().equals("ROLE_USER")));
    }

    @Test
    void deberiaLoguearUsuarioCorrectamente() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("camilo123");
        loginRequest.setPassword("claveSegura1");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setUsername("camilo123");
        usuarioGuardado.setRole("ROLE_USER");

        when(usuarioRepository.findByUsername("camilo123")).thenReturn(Optional.of(usuarioGuardado));
        when(jwtUtil.generarToken("camilo123", "ROLE_USER")).thenReturn("token");

        AuthResponse resultado = service.login(loginRequest);

        assertNotNull(resultado);
        assertEquals("token", resultado.getAccessToken());
        assertNotNull(resultado.getRefreshToken());
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository).findByUsername("camilo123");
    }

    @Test
    void deberiaLanzarErrorSiUsuarioNoExisteAlLoguear() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("usuarioInexistente");
        loginRequest.setPassword("claveSegura1");

        when(usuarioRepository.findByUsername("usuarioInexistente")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.login(loginRequest));

        assertEquals("Usuario inválido", ex.getMessage());
        verify(usuarioRepository).findByUsername("usuarioInexistente");
        verify(jwtUtil, never()).generarToken(any(), any());
    }

    @Test
    void deberiaRefrescarTokenCorrectamente() {
        String refreshTokenValor = "clave_super_secreta_12345678901234567890";

        RefreshToken tokenGuardado = new RefreshToken();
        tokenGuardado.setToken(refreshTokenValor);
        tokenGuardado.setUsername("camilo123");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setUsername("camilo123");
        usuarioGuardado.setRole("ROLE_USER");

        when(refreshRepository.findByToken(refreshTokenValor)).thenReturn(Optional.of(tokenGuardado));
        when(jwtUtil.esValido(refreshTokenValor)).thenReturn(true);
        when(jwtUtil.esRefreshToken(refreshTokenValor)).thenReturn(true);
        when(usuarioRepository.findByUsername("camilo123")).thenReturn(Optional.of(usuarioGuardado));
        when(jwtUtil.generarToken("camilo123", "ROLE_USER")).thenReturn("nuevoToken");

        AuthResponse resultado = service.refresh(refreshTokenValor);

        assertNotNull(resultado);
        assertEquals("nuevoToken", resultado.getAccessToken());
        assertEquals(refreshTokenValor, resultado.getRefreshToken());
        verify(refreshRepository).findByToken(refreshTokenValor);
    }

    @Test
    void deberiaLanzarErrorSiRefreshTokenNoExiste() {
        String refreshTokenValor = "tokenQueNoExiste";

        when(refreshRepository.findByToken(refreshTokenValor)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.refresh(refreshTokenValor));

        assertEquals("Refresh inválido", ex.getMessage());
        verify(refreshRepository).findByToken(refreshTokenValor);
    }

    @Test
    void deberiaLanzarErrorSiRefreshTokenNoEsValido() {
        String refreshTokenValor = "tokenInvalido";

        RefreshToken tokenGuardado = new RefreshToken();
        tokenGuardado.setToken(refreshTokenValor);
        tokenGuardado.setUsername("camilo123");

        when(refreshRepository.findByToken(refreshTokenValor)).thenReturn(Optional.of(tokenGuardado));
        when(jwtUtil.esValido(refreshTokenValor)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.refresh(refreshTokenValor));

        assertEquals("Refresh token inválido", ex.getMessage());
        verify(usuarioRepository, never()).findByUsername(any());
    }

    @Test
    void deberiaLanzarErrorSiUsuarioNoExisteAlRefrescar() {
        String refreshTokenValor = "clave_super_secreta_12345678901234567890";

        RefreshToken tokenGuardado = new RefreshToken();
        tokenGuardado.setToken(refreshTokenValor);
        tokenGuardado.setUsername("usuarioEliminado");

        when(refreshRepository.findByToken(refreshTokenValor)).thenReturn(Optional.of(tokenGuardado));
        when(jwtUtil.esValido(refreshTokenValor)).thenReturn(true);
        when(jwtUtil.esRefreshToken(refreshTokenValor)).thenReturn(true);
        when(usuarioRepository.findByUsername("usuarioEliminado")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.refresh(refreshTokenValor));

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(jwtUtil, never()).generarToken(any(), any());
    }
}