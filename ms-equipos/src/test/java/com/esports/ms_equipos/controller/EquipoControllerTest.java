package com.esports.ms_equipos.controller;

import com.esports.ms_equipos.dto.EquipoRequest;
import com.esports.ms_equipos.dto.EquipoResponse;
import com.esports.ms_equipos.dto.RosterHistoricoRequest;
import com.esports.ms_equipos.dto.StaffTecnicoRequest;
import com.esports.ms_equipos.security.JwtFilter;
import com.esports.ms_equipos.security.JwtUtil;
import com.esports.ms_equipos.service.EquipoService;
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

@WebMvcTest(controllers = EquipoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EquipoControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private EquipoService service;

        @MockitoBean
        private JwtFilter jwtFilter;

        @MockitoBean
        private JwtUtil jwtUtil;

        private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

        @Test
        void debeCrearEquipo() throws Exception {
                EquipoRequest equipoCrear = EquipoRequest.builder()
                                .nombreEquipo("SKT T1")
                                .region("LCK")
                                .fechaFundacion(LocalDate.parse("2004-04-13"))
                                .build();

                EquipoResponse equipoCreado = EquipoResponse.builder()
                                .id(1L)
                                .nombreEquipo("SKT T1")
                                .region("LCK")
                                .fechaFundacion(LocalDate.parse("2004-04-13"))
                                .activo(true)
                                .rankingMundial(1)
                                .cantidadStaff(0)
                                .cantidadRoster(0)
                                .build();

                when(service.crearEquipo(any(EquipoRequest.class))).thenReturn(equipoCreado);

                mockMvc.perform(post("/api/v1/equipos").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(equipoCrear)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("El Equipo ha sido creado con éxito"))
                                .andExpect(jsonPath("$.data.id").value(1L))
                                .andExpect(jsonPath("$.data.nombreEquipo").value("SKT T1"))
                                .andExpect(jsonPath("$.data.region").value("LCK"));
        }

        @Test
        void debeAgregarStaff() throws Exception {
                StaffTecnicoRequest peticion = StaffTecnicoRequest.builder()
                                .nombreStaff("Miguel")
                                .rol("HEAD_COACH")
                                .salarioMensual(15000.0)
                                .build();

                EquipoResponse respuesta = EquipoResponse.builder()
                                .id(1L)
                                .nombreEquipo("SKT T1")
                                .region("LCK")
                                .activo(true)
                                .cantidadStaff(1)
                                .cantidadRoster(0)
                                .build();

                when(service.agregarStaff(eq(1L), any(StaffTecnicoRequest.class))).thenReturn(respuesta);

                mockMvc.perform(post("/api/v1/equipos/1/staff")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(peticion)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Staff Agregado Correctamente"))
                                .andExpect(jsonPath("$.data.id").value(1L))
                                .andExpect(jsonPath("$.data.cantidadStaff").value(1));
        }

        @Test
        void debeAgregarJugador() throws Exception {
                RosterHistoricoRequest request = RosterHistoricoRequest.builder()
                                .idJugador(99L)
                                .fechaInicio(LocalDate.now())
                                .build();

                EquipoResponse response = EquipoResponse.builder()
                                .id(1L)
                                .nombreEquipo("SKT T1")
                                .region("LCK")
                                .activo(true)
                                .cantidadStaff(0)
                                .cantidadRoster(1)
                                .build();

                String token = "Token";

                when(service.agregarJugador(eq(1L), any(RosterHistoricoRequest.class), eq(token))).thenReturn(response);

                mockMvc.perform(post("/api/v1/equipos/1/roster")
                                .header("Authorization", token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Jugador agregado al Roster"))
                                .andExpect(jsonPath("$.data.id").value(1L))
                                .andExpect(jsonPath("$.data.cantidadRoster").value(1));
        }

        @Test
        void debeMostrarEquipos() throws Exception {
                EquipoResponse e1 = EquipoResponse.builder().id(1L).nombreEquipo("T1").region("LCK").activo(true)
                                .build();
                EquipoResponse e2 = EquipoResponse.builder().id(2L).nombreEquipo("G2").region("LEC").activo(true)
                                .build();
                List<EquipoResponse> lista = List.of(e1, e2);

                when(service.listarEquipos()).thenReturn(lista);

                mockMvc.perform(get("/api/v1/equipos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Lista mostrada con éxito"))
                                .andExpect(jsonPath("$.data[0].id").value(1L))
                                .andExpect(jsonPath("$.data[0].nombreEquipo").value("T1"))
                                .andExpect(jsonPath("$.data[1].id").value(2L))
                                .andExpect(jsonPath("$.data[1].nombreEquipo").value("G2"));
        }

        @Test
        void debeMostrarEquiposPorRegion() throws Exception {
                EquipoResponse e1 = EquipoResponse.builder().id(1L).nombreEquipo("T1").region("LCK").activo(true)
                                .build();
                List<EquipoResponse> lista = List.of(e1);

                when(service.listarEquiposPorRegion("LCK")).thenReturn(lista);

                mockMvc.perform(get("/api/v1/equipos/region/LCK"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Lista de equipos por región"))
                                .andExpect(jsonPath("$.data[0].region").value("LCK"))
                                .andExpect(jsonPath("$.data[0].nombreEquipo").value("T1"));
        }

        @Test
        void debeActualizarEquipo() throws Exception {
                EquipoRequest request = EquipoRequest.builder()
                                .nombreEquipo("T1 r Edition")
                                .region("LCK")
                                .fechaFundacion(LocalDate.parse("2004-04-13"))
                                .rankingMundial(1)
                                .activo(true)
                                .build();

                EquipoResponse respuestaEquipo = EquipoResponse.builder()
                                .id(1L)
                                .nombreEquipo("T1 r Edition")
                                .region("LCK")
                                .activo(true)
                                .build();

                when(service.actualizarEquipo(eq(1L), any(EquipoRequest.class))).thenReturn(respuestaEquipo);

                mockMvc.perform(put("/api/v1/equipos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Equipo Actualizado con éxito"))
                                .andExpect(jsonPath("$.data.id").value(1L))
                                .andExpect(jsonPath("$.data.nombreEquipo").value("T1 r Edition"));
        }

        @Test
        void debeDesactivarEquipo() throws Exception {
                doNothing().when(service).desactivarEquipo(1L);

                mockMvc.perform(delete("/api/v1/equipos/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void debeTerminarContrato() throws Exception {
                doNothing().when(service).terminarContrato(5L);

                mockMvc.perform(delete("/api/v1/equipos/roster/5"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void debeDespedirStaff() throws Exception {
                doNothing().when(service).despedirStaff(10L);

                mockMvc.perform(delete("/api/v1/equipos/staff/10"))
                                .andExpect(status().isNoContent());
        }
}
