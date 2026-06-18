package com.esports.ms_patrocinadores.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.esports.ms_patrocinadores.client.EquipoClient;
import com.esports.ms_patrocinadores.dto.EquipoResponse;
import com.esports.ms_patrocinadores.dto.PatrocinadorRequest;
import com.esports.ms_patrocinadores.dto.PatrocinadorResponse;
import com.esports.ms_patrocinadores.exception.EquipoNoEncontradoException;
import com.esports.ms_patrocinadores.model.Patrocinador;
import com.esports.ms_patrocinadores.repository.PatrocinadorRepository;

@ExtendWith(MockitoExtension.class)
public class PatrocinadorServiceTest {

    @Mock
    private PatrocinadorRepository patrocinadorRepository;

    @Mock
    private EquipoClient equipoClient;

    @InjectMocks
    private PatrocinadorService patrocinadorService;

    @Test
    void deberiaRegistrarNuevoPatrocinador() {
        PatrocinadorRequest peticion = new PatrocinadorRequest();
        peticion.setNombreMarca("Red Bull");
        peticion.setIdEquipo(10L);
        peticion.setMontoAnual(50000.0);
        peticion.setFechaInicio(LocalDate.now());
        peticion.setFechaFin(LocalDate.now().plusYears(1));

        EquipoResponse equipo = EquipoResponse.builder().id(10L).nombreEquipo("T1").build();

        when(equipoClient.obtenerEquipo(10L, "token")).thenReturn(equipo);
        when(patrocinadorRepository.save(any(Patrocinador.class))).thenAnswer(invocation -> {
            Patrocinador p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        PatrocinadorResponse resultado = patrocinadorService.registrarPatrocinador(peticion, "token");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Red Bull", resultado.getNombreMarca());
        assertEquals(10L, resultado.getIdEquipo());
        assertEquals(50000.0, resultado.getMontoAnual());

        verify(equipoClient).obtenerEquipo(10L, "token");
        verify(patrocinadorRepository).save(any(Patrocinador.class));
    }

    @Test
    void deberiaRetornarTodosLosPatrocinadores() {
        Patrocinador patrocinador = new Patrocinador();
        patrocinador.setId(1L);
        patrocinador.setNombreMarca("Logitech");

        when(patrocinadorRepository.findAll()).thenReturn(List.of(patrocinador));

        List<PatrocinadorResponse> resultado = patrocinadorService.listarTodosLosPatrocinadores();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Logitech", resultado.get(0).getNombreMarca());
        verify(patrocinadorRepository).findAll();
    }

    @Test
    void deberiaRetornarPatrocinadoresPorEquipo() {
        Patrocinador patrocinador = new Patrocinador();
        patrocinador.setIdEquipo(15L);
        patrocinador.setNombreMarca("Intel");

        when(patrocinadorRepository.findByIdEquipo(15L)).thenReturn(List.of(patrocinador));

        List<PatrocinadorResponse> resultado = patrocinadorService.buscarPatrocinadoresPorEquipo(15L);

        assertFalse(resultado.isEmpty());
        assertEquals("Intel", resultado.get(0).getNombreMarca());
        verify(patrocinadorRepository).findByIdEquipo(15L);
    }

    // Tests ERRORES

    @Test
    void deberiaLanzarEquipoNoEncontradoAlRegistrar() {
        PatrocinadorRequest peticion = new PatrocinadorRequest();
        peticion.setIdEquipo(99L);

        when(equipoClient.obtenerEquipo(99L, "token")).thenReturn(null);

        EquipoNoEncontradoException ex = assertThrows(EquipoNoEncontradoException.class,
                () -> patrocinadorService.registrarPatrocinador(peticion, "token"));

        assertNotNull(ex);
        assertEquals("Equipo con la ID: 99 no encontrado", ex.getMessage());
        verify(patrocinadorRepository, never()).save(any(Patrocinador.class));
    }
}