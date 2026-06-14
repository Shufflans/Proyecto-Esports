package com.esports.ms_jugadores.controller;

import com.esports.ms_jugadores.security.JwtFilter;
import com.esports.ms_jugadores.security.JwtUtil;
import com.esports.ms_jugadores.dto.JugadorRequest;
import com.esports.ms_jugadores.dto.JugadorResponse;
import com.esports.ms_jugadores.service.JugadorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = JugadorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class JugadoresControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JugadorService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void debeCrearJugador() throws Exception {
        JugadorRequest jugador = JugadorRequest.builder()
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("ADC")
                .salarioMensual(10000.0)
                .build();

        JugadorResponse jugadorCreado = JugadorResponse.builder()
                .id(1L)
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("ADC")
                .idEquipoActual(null)
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        when(service.crear(any(JugadorRequest.class))).thenReturn(jugadorCreado);

        mockMvc.perform(post("/api/v1/jugadores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jugador)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Jugador creado con éxito"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nickname").value(jugadorCreado.getNickname()))
                .andExpect(jsonPath("$.data.nombreReal").value(jugadorCreado.getNombreReal()));
    }

    @Test
    void debeListarJugadores() throws Exception {

        JugadorResponse jugadorCreado = JugadorResponse.builder()
                .id(1L)
                .nickname("nakaseomyces")
                .nombreReal("Perla Valentina")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-05-02"))
                .rol("SUPP")
                .idEquipoActual(null)
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        JugadorResponse jugadorCreado2 = JugadorResponse.builder()
                .id(2L)
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("ADC")
                .idEquipoActual(null)
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        List<JugadorResponse> guardados = List.of(jugadorCreado, jugadorCreado2);

        when(service.listarTodos()).thenReturn(guardados);

        mockMvc.perform(get("/api/v1/jugadores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lista de jugadores exitosa"))
                .andExpect(jsonPath("$.data[0].id").value(guardados.get(0).getId()))
                .andExpect(jsonPath("$.data[0].nickname").value(guardados.get(0).getNickname()));

    }

    @Test
    void debeBuscarAlJugadorPorLaId() throws Exception {
        JugadorResponse guardado = JugadorResponse.builder()
                .id(2L)
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("ADC")
                .idEquipoActual(null)
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        when(service.buscarPorId(2L)).thenReturn(guardado);

        mockMvc.perform(get("/api/v1/jugadores/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Jugador encontrado"))
                .andExpect(jsonPath("$.data.id").value(2L))
                .andExpect(jsonPath("$.data.nickname").value(guardado.getNickname()));
    }

    @Test
    void debeMostrarJugadoresPorPais() throws Exception {
        JugadorResponse guardar = JugadorResponse.builder()
                .id(1L)
                .nickname("Miguel")
                .pais("Chile")
                .build();

        List<JugadorResponse> guardado = List.of(guardar);

        when(service.mostrarPorPaises("Chile")).thenReturn(guardado);

        mockMvc.perform(get("/api/v1/jugadores/pais/Chile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Jugador encontrado"))
                .andExpect(jsonPath("$.data[0].id").value(guardado.get(0).getId()))
                .andExpect(jsonPath("$.data[0].nickname").value(guardado.get(0).getNickname()))
                .andExpect(jsonPath("$.data[0].pais").value(guardado.get(0).getPais()));
    }

    @Test
    void debeActualizarJugadores() throws Exception {
        JugadorResponse jugadorCreado = JugadorResponse.builder()
                .id(1L)
                .nickname("Ordep")
                .nombreReal("Pedro Gonzalez")
                .build();

        JugadorRequest jugadorNuevo = JugadorRequest.builder()
                .nickname("Pedro")
                .nombreReal("Pedro Gonzalez")
                .pais("Portugal")
                .fechaNacimiento(LocalDate.parse("1997-02-08"))
                .rol("JUNGLA")
                .salarioMensual(20000.0)
                .build();

        when(service.actualizar(eq(1L), any(JugadorRequest.class))).thenReturn(jugadorCreado);

        mockMvc.perform(put("/api/v1/jugadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jugadorNuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Jugador actualizado"))
                .andExpect(jsonPath("$.data.id").value(jugadorCreado.getId()))
                .andExpect(jsonPath("$.data.nickname").value(jugadorCreado.getNickname()))
                .andExpect(jsonPath("$.data.nombreReal").value(jugadorCreado.getNombreReal()));
    }

    @Test
    void debeDesactivarJugador() throws Exception {
        doNothing().when(service).desactivarJugador(1L);

        mockMvc.perform(delete("/api/v1/jugadores/1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Jugador desactivado"));

    }
}
