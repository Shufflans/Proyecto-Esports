package com.esports.ms_partidas.controller;

import com.esports.ms_partidas.dto.PartidaRequest;
import com.esports.ms_partidas.dto.PartidaResponse;
import com.esports.ms_partidas.dto.ResultadoPartidaRequest;
import com.esports.ms_partidas.security.JwtFilter;
import com.esports.ms_partidas.security.JwtUtil;
import com.esports.ms_partidas.service.PartidaService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PartidaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PartidaService partidaService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void debeCrearPartida() throws Exception {
        PartidaRequest peticion = PartidaRequest.builder()
                .idTorneo(10L)
                .idEquipoLocal(1L)
                .idEquipoVisitante(2L)
                .fechaHora(LocalDateTime.now())
                .build();

        PartidaResponse partidaRespuesta = PartidaResponse.builder()
                .id(1L)
                .idTorneo(10L)
                .nombreTorneo("Torneo Apertura")
                .idEquipoLocal(1L)
                .nombreEquipoLocal("Equipo A")
                .idEquipoVisitante(2L)
                .nombreEquipoVisitante("Equipo B")
                .estado("PROGRAMADA")
                .build();

        String token = "Bearer mock-token";

        when(partidaService.crearPartida(any(PartidaRequest.class), eq(token))).thenReturn(partidaRespuesta);

        mockMvc.perform(post("/api/v1/partidas")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(peticion)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Partida creada con éxito"))
                .andExpect(jsonPath("$.data.id").value(partidaRespuesta.getId()))
                .andExpect(jsonPath("$.data.estado").value(partidaRespuesta.getEstado()))
                .andExpect(jsonPath("$.data.nombreTorneo").value(partidaRespuesta.getNombreTorneo()));
    }

    @Test
    void debeFinalizarPartida() throws Exception {
        ResultadoPartidaRequest resultadoPeticion = ResultadoPartidaRequest.builder()
                .marcadorLocal(3)
                .marcadorVisitante(1)
                .duracionMinutos(45)
                .idEquipoGanador(1L)
                .build();

        PartidaResponse partidaFinalizada = PartidaResponse.builder()
                .id(1L)
                .idTorneo(10L)
                .marcadorLocal(3)
                .marcadorVisitante(1)
                .idEquipoGanador(1L)
                .estado("TERMINADA")
                .build();

        when(partidaService.finalizarPartida(eq(1L), any(ResultadoPartidaRequest.class))).thenReturn(partidaFinalizada);

        mockMvc.perform(put("/api/v1/partidas/1/finalizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resultadoPeticion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Partida finalizada con éxito"))
                .andExpect(jsonPath("$.data.id").value(partidaFinalizada.getId()))
                .andExpect(jsonPath("$.data.estado").value("TERMINADA"))
                .andExpect(jsonPath("$.data.idEquipoGanador").value(partidaFinalizada.getIdEquipoGanador()));
    }

    @Test
    void debeBuscarPartidaId() throws Exception {
        PartidaResponse guardada = PartidaResponse.builder()
                .id(5L)
                .nombreTorneo("Torneo Mundial")
                .estado("EN_JUEGO")
                .build();

        when(partidaService.obtenerPorId(5L)).thenReturn(guardada);

        mockMvc.perform(get("/api/v1/partidas/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Partida Encontrada"))
                .andExpect(jsonPath("$.data.id").value(5L))
                .andExpect(jsonPath("$.data.nombreTorneo").value(guardada.getNombreTorneo()))
                .andExpect(jsonPath("$.data.estado").value(guardada.getEstado()));
    }

    @Test
    void debeMostrarTodasLasPartidas() throws Exception {
        PartidaResponse partida1 = PartidaResponse.builder()
                .id(1L)
                .nombreTorneo("Torneo A")
                .estado("PROGRAMADA")
                .build();

        PartidaResponse partida2 = PartidaResponse.builder()
                .id(2L)
                .nombreTorneo("Torneo B")
                .estado("TERMINADA")
                .build();

        List<PartidaResponse> guardadas = List.of(partida1, partida2);

        when(partidaService.listarTodas()).thenReturn(guardadas);

        mockMvc.perform(get("/api/v1/partidas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Partidas Encontradas"))
                .andExpect(jsonPath("$.data[0].id").value(guardadas.get(0).getId()))
                .andExpect(jsonPath("$.data[0].nombreTorneo").value(guardadas.get(0).getNombreTorneo()))
                .andExpect(jsonPath("$.data[1].id").value(guardadas.get(1).getId()))
                .andExpect(jsonPath("$.data[1].estado").value(guardadas.get(1).getEstado()));
    }
}