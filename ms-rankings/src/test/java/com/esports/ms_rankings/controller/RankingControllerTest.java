package com.esports.ms_rankings.controller;

import com.esports.ms_rankings.dto.RankingRequest;
import com.esports.ms_rankings.dto.RankingResponse;
import com.esports.ms_rankings.security.JwtFilter;
import com.esports.ms_rankings.security.JwtUtil;
import com.esports.ms_rankings.service.RankingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RankingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RankingService rankingService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void debeRegistrarRanking() throws Exception {
        RankingRequest peticion = RankingRequest.builder()
                .idJugador(1L)
                .idEquipo(10L)
                .puntos(150)
                .build();

        RankingResponse rankingRespuesta = RankingResponse.builder()
                .id(1L)
                .idJugador(1L)
                .nombreJugador("Faker")
                .idEquipo(10L)
                .nombreEquipo("T1")
                .puntos(150)
                .fechaActualizacion(LocalDateTime.now())
                .build();

        String token = "Bearer mock-token";

        when(rankingService.registrarRanking(any(RankingRequest.class), eq(token))).thenReturn(rankingRespuesta);

        mockMvc.perform(post("/api/v1/rankings")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(peticion)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ranking registrado con éxito"))
                .andExpect(jsonPath("$.data.id").value(rankingRespuesta.getId()))
                .andExpect(jsonPath("$.data.nombreJugador").value(rankingRespuesta.getNombreJugador()))
                .andExpect(jsonPath("$.data.puntos").value(rankingRespuesta.getPuntos()));
    }

    @Test
    void debeMostrarTodosLosRankings() throws Exception {
        RankingResponse ranking1 = RankingResponse.builder()
                .id(1L)
                .idJugador(1L)
                .nombreJugador("Faker")
                .puntos(1500)
                .build();

        RankingResponse ranking2 = RankingResponse.builder()
                .id(2L)
                .idJugador(2L)
                .nombreJugador("Chovy")
                .puntos(1450)
                .build();

        List<RankingResponse> guardados = List.of(ranking1, ranking2);

        when(rankingService.listarTodosLosRankings()).thenReturn(guardados);

        mockMvc.perform(get("/api/v1/rankings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rankings encontrados"))
                .andExpect(jsonPath("$.data[0].id").value(guardados.get(0).getId()))
                .andExpect(jsonPath("$.data[0].nombreJugador").value(guardados.get(0).getNombreJugador()))
                .andExpect(jsonPath("$.data[1].id").value(guardados.get(1).getId()))
                .andExpect(jsonPath("$.data[1].nombreJugador").value(guardados.get(1).getNombreJugador()));
    }

    @Test
    void debeBuscarRankingsPorJugador() throws Exception {
        RankingResponse ranking = RankingResponse.builder()
                .id(5L)
                .idJugador(10L)
                .nombreJugador("ShowMaker")
                .puntos(1200)
                .build();

        List<RankingResponse> guardados = List.of(ranking);

        when(rankingService.listarRankingsPorJugador(10L)).thenReturn(guardados);

        mockMvc.perform(get("/api/v1/rankings/jugador/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rankings del jugador encontrados"))
                .andExpect(jsonPath("$.data[0].id").value(ranking.getId()))
                .andExpect(jsonPath("$.data[0].nombreJugador").value(ranking.getNombreJugador()))
                .andExpect(jsonPath("$.data[0].puntos").value(ranking.getPuntos()));
    }

    @Test
    void debeBuscarRankingsPorEquipo() throws Exception {
        RankingResponse ranking = RankingResponse.builder()
                .id(7L)
                .idEquipo(20L)
                .nombreEquipo("G2 Esports")
                .puntos(900)
                .build();

        List<RankingResponse> guardados = List.of(ranking);

        when(rankingService.listarRankingsPorEquipo(20L)).thenReturn(guardados);

        mockMvc.perform(get("/api/v1/rankings/equipo/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rankings del equipo encontrados"))
                .andExpect(jsonPath("$.data[0].id").value(ranking.getId()))
                .andExpect(jsonPath("$.data[0].nombreEquipo").value(ranking.getNombreEquipo()))
                .andExpect(jsonPath("$.data[0].puntos").value(ranking.getPuntos()));
    }

    @Test
    void debeEliminarRanking() throws Exception {
        Long idRankingAEliminar = 1L;

        doNothing().when(rankingService).eliminarRanking(idRankingAEliminar);

        mockMvc.perform(delete("/api/v1/rankings/" + idRankingAEliminar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ranking eliminado con éxito"));
    }
}