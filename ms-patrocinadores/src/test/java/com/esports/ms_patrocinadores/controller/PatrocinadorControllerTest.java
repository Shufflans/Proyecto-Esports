package com.esports.ms_patrocinadores.controller;

import com.esports.ms_patrocinadores.dto.PatrocinadorRequest;
import com.esports.ms_patrocinadores.dto.PatrocinadorResponse;
import com.esports.ms_patrocinadores.security.JwtFilter;
import com.esports.ms_patrocinadores.security.JwtUtil;
import com.esports.ms_patrocinadores.service.PatrocinadorService;
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

@WebMvcTest(controllers = PatrocinadorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PatrocinadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatrocinadorService patrocinadorService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void debeRegistrarPatrocinador() throws Exception {
        PatrocinadorRequest peticion = new PatrocinadorRequest();
        peticion.setNombreMarca("Red Bull");
        peticion.setIdEquipo(10L);
        peticion.setMontoAnual(50000.0);
        peticion.setFechaInicio(LocalDate.now());
        peticion.setFechaFin(LocalDate.now().plusYears(1));

        PatrocinadorResponse patrocinadorRespuesta = PatrocinadorResponse.builder()
                .id(1L)
                .nombreMarca("Red Bull")
                .idEquipo(10L)
                .montoAnual(50000.0)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .build();

        String token = "Bearer mock-token";

        when(patrocinadorService.registrarPatrocinador(any(PatrocinadorRequest.class), eq(token)))
                .thenReturn(patrocinadorRespuesta);

        mockMvc.perform(post("/api/v1/patrocinadores")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(peticion)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Patrocinador Registrado con éxito"))
                .andExpect(jsonPath("$.data.id").value(patrocinadorRespuesta.getId()))
                .andExpect(jsonPath("$.data.nombreMarca").value(patrocinadorRespuesta.getNombreMarca()))
                .andExpect(jsonPath("$.data.idEquipo").value(patrocinadorRespuesta.getIdEquipo()))
                .andExpect(jsonPath("$.data.montoAnual").value(patrocinadorRespuesta.getMontoAnual()));
    }

    @Test
    void debeMostrarTodosLosPatrocinadores() throws Exception {
        PatrocinadorResponse patrocinador1 = PatrocinadorResponse.builder()
                .id(1L)
                .nombreMarca("Logitech")
                .idEquipo(15L)
                .montoAnual(25000.0)
                .build();

        PatrocinadorResponse patrocinador2 = PatrocinadorResponse.builder()
                .id(2L)
                .nombreMarca("Intel")
                .idEquipo(15L)
                .montoAnual(40000.0)
                .build();

        List<PatrocinadorResponse> guardados = List.of(patrocinador1, patrocinador2);

        when(patrocinadorService.listarTodosLosPatrocinadores()).thenReturn(guardados);

        mockMvc.perform(get("/api/v1/patrocinadores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Patrocinadores Listados con éxito"))
                .andExpect(jsonPath("$.data[0].id").value(guardados.get(0).getId()))
                .andExpect(jsonPath("$.data[0].nombreMarca").value(guardados.get(0).getNombreMarca()))
                .andExpect(jsonPath("$.data[1].id").value(guardados.get(1).getId()))
                .andExpect(jsonPath("$.data[1].nombreMarca").value(guardados.get(1).getNombreMarca()));
    }

    @Test
    void debeBuscarPatrocinadoresPorEquipo() throws Exception {
        PatrocinadorResponse patrocinador = PatrocinadorResponse.builder()
                .id(7L)
                .nombreMarca("Secretlab")
                .idEquipo(20L)
                .montoAnual(15000.0)
                .build();

        List<PatrocinadorResponse> guardados = List.of(patrocinador);

        when(patrocinadorService.buscarPatrocinadoresPorEquipo(20L)).thenReturn(guardados);

        mockMvc.perform(get("/api/v1/patrocinadores/equipo/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Patrocinadores por Equipo listados"))
                .andExpect(jsonPath("$.data[0].id").value(patrocinador.getId()))
                .andExpect(jsonPath("$.data[0].nombreMarca").value(patrocinador.getNombreMarca()))
                .andExpect(jsonPath("$.data[0].idEquipo").value(patrocinador.getIdEquipo()));
    }
}