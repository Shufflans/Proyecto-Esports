package com.esports.ms_estadisticas.controller;

import com.esports.ms_estadisticas.dto.EstadisticaRequest;
import com.esports.ms_estadisticas.dto.EstadisticaResponse;
import com.esports.ms_estadisticas.security.JwtFilter;
import com.esports.ms_estadisticas.security.JwtUtil;
import com.esports.ms_estadisticas.service.EstadisticaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EstadisticaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EstadisticaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstadisticaService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void debeCrearEstadistica() throws Exception {
        EstadisticaRequest request = EstadisticaRequest.builder()
                .idJugador(1L)
                .idPartida(10L)
                .asesinatos(12)
                .muertes(3)
                .asistencias(8)
                .mvp(true)
                .build();

        EstadisticaResponse response = EstadisticaResponse.builder()
                .id(1L)
                .idJugador(1L)
                .idPartida(10L)
                .nicknameJugador("Faker")
                .asesinatos(12)
                .muertes(3)
                .asistencias(8)
                .kda(6.67)
                .mvp(true)
                .build();

        String token = "Token";

        when(service.crearEstadistica(any(EstadisticaRequest.class), eq(token))).thenReturn(response);

        mockMvc.perform(post("/api/v1/estadisticas")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Estadística creada con éxito"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.idJugador").value(1L))
                .andExpect(jsonPath("$.data.idPartida").value(10L))
                .andExpect(jsonPath("$.data.nicknameJugador").value("Faker"))
                .andExpect(jsonPath("$.data.kda").value(6.67))
                .andExpect(jsonPath("$.data.mvp").value(true));
    }

    @Test
    void debeBuscarEstadisticasPorJugador() throws Exception {
        EstadisticaResponse e1 = EstadisticaResponse.builder()
                .id(1L)
                .idJugador(1L)
                .idPartida(10L)
                .nicknameJugador("Faker")
                .kda(6.67)
                .build();

        List<EstadisticaResponse> lista = List.of(e1);

        when(service.listarEstadisticasPorJugador(1L)).thenReturn(lista);

        mockMvc.perform(get("/api/v1/estadisticas/jugador/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Estadísticas del Jugador Encontradas"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].idJugador").value(1L))
                .andExpect(jsonPath("$.data[0].nicknameJugador").value("Faker"));
    }

    @Test
    void debeBuscarEstadisticasPorPartida() throws Exception {
        EstadisticaResponse e1 = EstadisticaResponse.builder()
                .id(1L)
                .idJugador(1L)
                .idPartida(10L)
                .nicknameJugador("Faker")
                .kda(6.67)
                .build();

        List<EstadisticaResponse> lista = List.of(e1);

        when(service.listarEstadisticasPorPartida(10L)).thenReturn(lista);

        mockMvc.perform(get("/api/v1/estadisticas/partida/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Estadísticas de la Partida Encontradas"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].idPartida").value(10L))
                .andExpect(jsonPath("$.data[0].nicknameJugador").value("Faker"));
    }
}