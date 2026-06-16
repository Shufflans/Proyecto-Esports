package com.esports.ms_juegos.controller;

import com.esports.ms_juegos.dto.JuegoRequest;
import com.esports.ms_juegos.dto.JuegoResponse;
import com.esports.ms_juegos.security.JwtFilter;
import com.esports.ms_juegos.security.JwtUtil;
import com.esports.ms_juegos.service.JuegoService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = JuegoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class JuegoControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private JuegoService juegoService;

        @MockitoBean
        private JwtFilter jwtFilter;

        @MockitoBean
        private JwtUtil jwtUtil;

        private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

        @Test
        void debeCrearJuego() throws Exception {
                JuegoRequest peticion = JuegoRequest.builder()
                                .nombreJuego("League of Legends")
                                .generoJuego("MOBA")
                                .build();

                JuegoResponse juegoRespuesta = JuegoResponse.builder()
                                .id(1L)
                                .nombreJuego("League of Legends")
                                .generoJuego("MOBA")
                                .activo(true)
                                .build();

                when(juegoService.crearJuego(any(JuegoRequest.class))).thenReturn(juegoRespuesta);

                mockMvc.perform(post("/api/v1/juegos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(peticion)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Juego Creado con éxito"))
                                .andExpect(jsonPath("$.data.id").value(juegoRespuesta.getId()))
                                .andExpect(jsonPath("$.data.nombreJuego").value(juegoRespuesta.getNombreJuego()))
                                .andExpect(jsonPath("$.data.generoJuego").value(juegoRespuesta.getGeneroJuego()));
        }

        @Test
        void debeBuscarJuegoId() throws Exception {
                JuegoResponse guardado = JuegoResponse.builder()
                                .id(2L)
                                .nombreJuego("Valorant")
                                .generoJuego("Shooter")
                                .activo(true)
                                .build();

                when(juegoService.buscarJuegoPorId(2L)).thenReturn(guardado);

                mockMvc.perform(get("/api/v1/juegos/2"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Juego Encontrado"))
                                .andExpect(jsonPath("$.data.id").value(2L))
                                .andExpect(jsonPath("$.data.nombreJuego").value(guardado.getNombreJuego()));
        }

        @Test
        void debeMostrarTodosLosJuegos() throws Exception {
                JuegoResponse juego = JuegoResponse.builder()
                                .id(1L)
                                .nombreJuego("Dota 2")
                                .generoJuego("MOBA")
                                .activo(true)
                                .build();

                JuegoResponse juego2 = JuegoResponse.builder()
                                .id(2L)
                                .nombreJuego("Counter Strike 2")
                                .generoJuego("Shooter")
                                .activo(true)
                                .build();

                List<JuegoResponse> guardados = List.of(juego, juego2);

                when(juegoService.listarTodosLosJuegos()).thenReturn(guardados);

                mockMvc.perform(get("/api/v1/juegos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Juego Encontrado"))
                                .andExpect(jsonPath("$.data[0].id").value(guardados.get(0).getId()))
                                .andExpect(jsonPath("$.data[0].nombreJuego").value(guardados.get(0).getNombreJuego()));
        }

        @Test
        void debeMostrarJuegosActivos() throws Exception {
                JuegoResponse juego = JuegoResponse.builder()
                                .id(1L)
                                .nombreJuego("Rocket League")
                                .generoJuego("Deportes")
                                .activo(true)
                                .build();

                List<JuegoResponse> guardados = List.of(juego);

                when(juegoService.listarJuegosActivos()).thenReturn(guardados);

                mockMvc.perform(get("/api/v1/juegos/activos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Juegos Activos Encontrados"))
                                .andExpect(jsonPath("$.data[0].id").value(guardados.get(0).getId()))
                                .andExpect(jsonPath("$.data[0].nombreJuego").value(guardados.get(0).getNombreJuego()));
        }

        @Test
        void debeMostrarJuegosPorGenero() throws Exception {
                JuegoResponse juego = JuegoResponse.builder()
                                .id(1L)
                                .nombreJuego("Street Fighter 6")
                                .generoJuego("Peleas")
                                .activo(true)
                                .build();

                List<JuegoResponse> guardados = List.of(juego);

                when(juegoService.buscarJuegoPorGenero("Peleas")).thenReturn(guardados);

                mockMvc.perform(get("/api/v1/juegos/genero/Peleas"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Juegos por Generos listados"))
                                .andExpect(jsonPath("$.data[0].id").value(guardados.get(0).getId()))
                                .andExpect(jsonPath("$.data[0].generoJuego").value(guardados.get(0).getGeneroJuego()));
        }

        @Test
        void debeActualizarJuego() throws Exception {
                JuegoRequest juegoNuevo = JuegoRequest.builder()
                                .nombreJuego("Overwatch 2")
                                .generoJuego("Shooter")
                                .build();

                JuegoResponse guardado = JuegoResponse.builder()
                                .id(1L)
                                .nombreJuego("Overwatch 2")
                                .generoJuego("Shooter")
                                .activo(true)
                                .build();

                when(juegoService.actualizarJuego(eq(1L), any(JuegoRequest.class))).thenReturn(guardado);

                mockMvc.perform(put("/api/v1/juegos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(juegoNuevo)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Juego actualizado con éxito"))
                                .andExpect(jsonPath("$.data.id").value(guardado.getId()))
                                .andExpect(jsonPath("$.data.nombreJuego").value(guardado.getNombreJuego()));
        }

        @Test
        void debeDesactivarJuego() throws Exception {
                doNothing().when(juegoService).desactivarJuego(1L);

                mockMvc.perform(delete("/api/v1/juegos/1"))
                                .andExpect(status().isNoContent());
        }
}
