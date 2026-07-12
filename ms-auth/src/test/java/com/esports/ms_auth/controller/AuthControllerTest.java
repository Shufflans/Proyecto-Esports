package com.esports.ms_auth.controller;

import com.esports.ms_auth.dto.AuthResponse;
import com.esports.ms_auth.dto.LoginRequest;
import com.esports.ms_auth.dto.RefreshTokenRequest;
import com.esports.ms_auth.dto.RegisterRequest;
import com.esports.ms_auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void debeRegistrarUsuario() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("camilo123");
        request.setPassword("claveSegura1");

        AuthResponse respuesta = new AuthResponse("tokenAcceso", "tokenRefresh");

        when(authService.register(any(RegisterRequest.class))).thenReturn(respuesta);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario registrado exitosamente"))
                .andExpect(jsonPath("$.data.accessToken").value("tokenAcceso"))
                .andExpect(jsonPath("$.data.refreshToken").value("tokenRefresh"));
    }

    @Test
    void debeRechazarRegistroConUsernameMuyCorto() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("ab");
        request.setPassword("claveSegura1");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeLoguearUsuario() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("camilo123");
        request.setPassword("claveSegura1");

        AuthResponse respuesta = new AuthResponse("tokenAcceso", "tokenRefresh");

        when(authService.login(any(LoginRequest.class))).thenReturn(respuesta);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("El usuario se ha logeado con éxito"))
                .andExpect(jsonPath("$.data.accessToken").value("tokenAcceso"));
    }

    @Test
    void debeRechazarLoginConPasswordMuyCorta() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("camilo123");
        request.setPassword("123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRefrescarToken() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("clave_super_secreta_12345678901234567890");

        AuthResponse respuesta = new AuthResponse("nuevoTokenAcceso", "clave_super_secreta_12345678901234567890");

        when(authService.refresh("clave_super_secreta_12345678901234567890")).thenReturn(respuesta);

        mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Token renovado"))
                .andExpect(jsonPath("$.data.accessToken").value("nuevoTokenAcceso"));
    }
}