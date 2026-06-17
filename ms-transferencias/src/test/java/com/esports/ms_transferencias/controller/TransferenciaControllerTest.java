package com.esports.ms_transferencias.controller;

import com.esports.ms_transferencias.dto.TransferenciaRequest;
import com.esports.ms_transferencias.dto.TransferenciaResponse;
import com.esports.ms_transferencias.security.JwtFilter;
import com.esports.ms_transferencias.security.JwtUtil;
import com.esports.ms_transferencias.service.TransferenciaService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransferenciaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransferenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransferenciaService transferenciaService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void debeRegistrarTransferencia() throws Exception {
        TransferenciaRequest peticion = TransferenciaRequest.builder()
                .idJugador(10L)
                .idEquipoOrigen(1L)
                .idEquipoDestino(2L)
                .fechaTransferencia(java.time.LocalDate.now()) // <-- Obligatorio (@NotNull)
                .montoUsd(50000.0) // <-- Obligatorio (@Positive)
                .tipo("TRANSFERENCIA") // <-- Obligatorio (@NotNull)
                .build();

        TransferenciaResponse transferenciaRespuesta = TransferenciaResponse.builder()
                .id(1L)
                .idJugador(10L)
                .nicknameJugador("Faker")
                .idEquipoOrigen(1L)
                .nombreEquipoOrigen("T1")
                .idEquipoDestino(2L)
                .nombreEquipoDestino("Gen.G")
                .tipo("TRANSFERENCIA")
                .fechaTransferencia(LocalDate.now())
                .build();

        String token = "Bearer mock-token";

        when(transferenciaService.registrarTransferencia(any(TransferenciaRequest.class), eq(token)))
                .thenReturn(transferenciaRespuesta);

        mockMvc.perform(post("/api/v1/transferencias")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(peticion)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transferencia Registrada con éxito"))
                .andExpect(jsonPath("$.data.id").value(transferenciaRespuesta.getId()))
                .andExpect(jsonPath("$.data.tipo").value(transferenciaRespuesta.getTipo()))
                .andExpect(jsonPath("$.data.nicknameJugador").value(transferenciaRespuesta.getNicknameJugador()));
    }

    @Test
    void debeMostrarTodasLasTransferencias() throws Exception {
        TransferenciaResponse t1 = TransferenciaResponse.builder()
                .id(1L)
                .idJugador(5L)
                .tipo("FICHAJE_INICIAL")
                .build();

        TransferenciaResponse t2 = TransferenciaResponse.builder()
                .id(2L)
                .idJugador(10L)
                .tipo("PRESTAMO")
                .build();

        List<TransferenciaResponse> guardadas = List.of(t1, t2);

        when(transferenciaService.listarTodasLasTransferencias()).thenReturn(guardadas);

        mockMvc.perform(get("/api/v1/transferencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transferencias Listadas con éxito"))
                .andExpect(jsonPath("$.data[0].id").value(guardadas.get(0).getId()))
                .andExpect(jsonPath("$.data[0].tipo").value(guardadas.get(0).getTipo()))
                .andExpect(jsonPath("$.data[1].id").value(guardadas.get(1).getId()))
                .andExpect(jsonPath("$.data[1].tipo").value(guardadas.get(1).getTipo()));
    }

    @Test
    void debeMostrarTransferenciasPorJugador() throws Exception {
        TransferenciaResponse t1 = TransferenciaResponse.builder()
                .id(1L)
                .idJugador(7L)
                .nicknameJugador("Chovy")
                .tipo("FICHAJE_INICIAL")
                .build();

        List<TransferenciaResponse> guardadas = List.of(t1);

        when(transferenciaService.buscarTransferenciasPorJugador(7L)).thenReturn(guardadas);

        mockMvc.perform(get("/api/v1/transferencias/jugador/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transferencias por Jugador listadas"))
                .andExpect(jsonPath("$.data[0].id").value(guardadas.get(0).getId()))
                .andExpect(jsonPath("$.data[0].idJugador").value(guardadas.get(0).getIdJugador()))
                .andExpect(jsonPath("$.data[0].nicknameJugador").value(guardadas.get(0).getNicknameJugador()));
    }

    @Test
    void debeMostrarTransferenciasPorTipo() throws Exception {
        TransferenciaResponse t1 = TransferenciaResponse.builder()
                .id(3L)
                .idJugador(15L)
                .tipo("BAJA")
                .build();

        TransferenciaResponse t2 = TransferenciaResponse.builder()
                .id(4L)
                .idJugador(20L)
                .tipo("BAJA")
                .build();

        List<TransferenciaResponse> guardadas = List.of(t1, t2);

        when(transferenciaService.buscarTransferenciasPorTipo("BAJA")).thenReturn(guardadas);

        mockMvc.perform(get("/api/v1/transferencias/tipo/BAJA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transferencias por Tipo listadas"))
                .andExpect(jsonPath("$.data[0].id").value(guardadas.get(0).getId()))
                .andExpect(jsonPath("$.data[0].tipo").value("BAJA"))
                .andExpect(jsonPath("$.data[1].id").value(guardadas.get(1).getId()))
                .andExpect(jsonPath("$.data[1].tipo").value("BAJA"));
    }
}