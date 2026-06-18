package com.esports.ms_torneos.controller;

import com.esports.ms_torneos.dto.TorneoRequest;
import com.esports.ms_torneos.dto.TorneoResponse;
import com.esports.ms_torneos.security.JwtFilter;
import com.esports.ms_torneos.security.JwtUtil;
import com.esports.ms_torneos.service.TorneoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TorneoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TorneoControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private TorneoService torneoService;

        @MockitoBean
        private JwtFilter jwtFilter;

        @MockitoBean
        private JwtUtil jwtUtil;

        private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

        @Test
        void debeCrearTorneo() throws Exception {
                TorneoRequest peticion = TorneoRequest.builder()
                                .idJuego(1L)
                                .nombreTorneo("LEC Otoño 2025")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(2))
                                .totalPremio(50000.0)
                                .maxEquipos(8)
                                .build();

                TorneoResponse torneoRespuesta = TorneoResponse.builder()
                                .id(1L)
                                .nombreTorneo("LEC Otoño 2025")
                                .build();

                when(torneoService.crearTorneo(any(TorneoRequest.class), any(String.class)))
                                .thenReturn(torneoRespuesta);

                mockMvc.perform(post("/api/v1/torneos")
                                .header("Authorization", "Bearer token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(peticion)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Torneo Creado con éxito"))
                                .andExpect(jsonPath("$.data.id").value(torneoRespuesta.getId()))
                                .andExpect(jsonPath("$.data.nombreTorneo").value(torneoRespuesta.getNombreTorneo()));
        }

        @Test
        void debeBuscarTorneoId() throws Exception {
                TorneoResponse guardado = TorneoResponse.builder()
                                .id(2L)
                                .nombreTorneo("Worlds 2025")
                                .build();

                when(torneoService.buscarPorId(2L)).thenReturn(guardado);

                mockMvc.perform(get("/api/v1/torneos/2"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Torneo Encontrado"))
                                .andExpect(jsonPath("$.data.id").value(2L))
                                .andExpect(jsonPath("$.data.nombreTorneo").value(guardado.getNombreTorneo()));
        }

        @Test
        void debeMostrarTodosLosTorneos() throws Exception {
                TorneoResponse torneo = TorneoResponse.builder()
                                .id(1L)
                                .nombreTorneo("Worlds 2026")
                                .build();

                TorneoResponse torneo2 = TorneoResponse.builder()
                                .id(2L)
                                .nombreTorneo("Mundial 2026")
                                .build();

                List<TorneoResponse> guardados = List.of(torneo, torneo2);

                when(torneoService.listarTodosLosTorneos()).thenReturn(guardados);

                mockMvc.perform(get("/api/v1/torneos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Torneos Listados con éxito"))
                                .andExpect(jsonPath("$.data[0].id").value(guardados.get(0).getId()))
                                .andExpect(jsonPath("$.data[0].nombreTorneo")
                                                .value(guardados.get(0).getNombreTorneo()));
        }

        @Test
        void debeMostrarTorneosPorEstado() throws Exception {
                TorneoResponse torneo = TorneoResponse.builder()
                                .id(1L)
                                .nombreTorneo("LCS Verano 2025")
                                .build();

                List<TorneoResponse> guardados = List.of(torneo);

                when(torneoService.listarTorneosPorEstado("PROGRAMADO")).thenReturn(guardados);

                mockMvc.perform(get("/api/v1/torneos/estado/PROGRAMADO"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Torneos por Estado listados"))
                                .andExpect(jsonPath("$.data[0].id").value(guardados.get(0).getId()))
                                .andExpect(jsonPath("$.data[0].nombreTorneo")
                                                .value(guardados.get(0).getNombreTorneo()));
        }

        @Test
        void debeMostrarTorneosPorIdJuego() throws Exception {
                TorneoResponse torneo = TorneoResponse.builder()
                                .id(1L)
                                .nombreTorneo("MSI 2025")
                                .build();

                List<TorneoResponse> guardados = List.of(torneo);

                when(torneoService.listarTorneosPorIdJuego(1L)).thenReturn(guardados);

                mockMvc.perform(get("/api/v1/torneos/juego/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Torneos por Juego listados"))
                                .andExpect(jsonPath("$.data[0].id").value(guardados.get(0).getId()))
                                .andExpect(jsonPath("$.data[0].nombreTorneo")
                                                .value(guardados.get(0).getNombreTorneo()));
        }

        @Test
        void debeActualizarTorneo() throws Exception {
                TorneoRequest torneoNuevo = TorneoRequest.builder()
                                .idJuego(1L)
                                .nombreTorneo("LEC Invierno 2025")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(3))
                                .totalPremio(75000.0)
                                .maxEquipos(16)
                                .build();

                TorneoResponse guardado = TorneoResponse.builder()
                                .id(1L)
                                .nombreTorneo("LEC Invierno 2025")
                                .build();

                when(torneoService.actualizarTorneo(eq(1L), any(TorneoRequest.class))).thenReturn(guardado);

                mockMvc.perform(put("/api/v1/torneos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(torneoNuevo)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Torneo actualizado con éxito"))
                                .andExpect(jsonPath("$.data.id").value(guardado.getId()))
                                .andExpect(jsonPath("$.data.nombreTorneo").value(guardado.getNombreTorneo()));
        }

        @Test
        void debeDesactivarTorneo() throws Exception {
                doNothing().when(torneoService).desactivarTorneo(1L);

                mockMvc.perform(delete("/api/v1/torneos/1"))
                                .andExpect(status().isNoContent());
        }
}