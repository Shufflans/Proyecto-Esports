package com.esports.ms_auth.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.esports.ms_auth.model.RefreshToken;
import com.esports.ms_auth.model.Usuario;

import java.sql.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void deberiaGuardarYBuscarUsuarioPorUsername() {
        Usuario usuario = new Usuario();
        usuario.setUsername("camilo123");
        usuario.setPassword("claveEncriptada");
        usuario.setRole("ROLE_USER");

        usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findByUsername("camilo123");

        assertTrue(resultado.isPresent());
        assertEquals("camilo123", resultado.get().getUsername());
        assertEquals("ROLE_USER", resultado.get().getRole());
    }

    @Test
    void deberiaRetornarVacioSiUsernameNoExiste() {
        Optional<Usuario> resultado = usuarioRepository.findByUsername("usuarioQueNoExiste");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deberiaAsignarIdAutomaticoAlGuardarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("nuevoUsuario");
        usuario.setPassword("clave123");
        usuario.setRole("ROLE_USER");

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        assertNotNull(usuarioGuardado.getId());
    }

    @Test
    void deberiaGuardarYBuscarRefreshTokenPorToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("550e8400-e29b-41d4-a716-446655440000");
        refreshToken.setUsername("camilo123");
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));

        refreshTokenRepository.save(refreshToken);

        Optional<RefreshToken> resultado = refreshTokenRepository
                .findByToken("550e8400-e29b-41d4-a716-446655440000");

        assertTrue(resultado.isPresent());
        assertEquals("camilo123", resultado.get().getUsername());
    }

    @Test
    void deberiaRetornarVacioSiTokenNoExiste() {
        Optional<RefreshToken> resultado = refreshTokenRepository.findByToken("tokenQueNoExiste");

        assertTrue(resultado.isEmpty());
    }
}