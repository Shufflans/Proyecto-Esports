package com.esports.ms_equipos.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.esports.ms_equipos.client.JugadorClient;
import com.esports.ms_equipos.dto.EquipoRequest;
import com.esports.ms_equipos.dto.EquipoResponse;
import com.esports.ms_equipos.dto.JugadorResponse;
import com.esports.ms_equipos.dto.RosterHistoricoRequest;
import com.esports.ms_equipos.dto.StaffTecnicoRequest;
import com.esports.ms_equipos.exception.EquipoNoEncontradoException;
import com.esports.ms_equipos.exception.JugadorNoExisteException;
import com.esports.ms_equipos.exception.NombreYRegionYaExistenException;
import com.esports.ms_equipos.exception.RosterIdNoEncontradoException;
import com.esports.ms_equipos.exception.StaffNoEncontradoException;
import com.esports.ms_equipos.exception.YaExisteElHeadCoachException;
import com.esports.ms_equipos.model.Equipo;
import com.esports.ms_equipos.model.RosterHistorico;
import com.esports.ms_equipos.model.StaffTecnico;
import com.esports.ms_equipos.repository.EquipoRepository;
import com.esports.ms_equipos.repository.RosterHistoricoRepository;
import com.esports.ms_equipos.repository.StaffTecnicoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipoServiceTest {
    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private RosterHistoricoRepository rosterHistoricoRepository;

    @Mock
    private StaffTecnicoRepository staffTecnicoRepository;

    @Mock
    private JugadorClient jugadorClient;

    @InjectMocks
    private EquipoService service;

    @Test
    void deberiaCrearEquipo() {
        EquipoRequest equipo = EquipoRequest.builder()
                .nombreEquipo("Kru")
                .region("Latam")
                .build();

        Equipo equipoGuardado = Equipo.builder()
                .id(1L)
                .nombreEquipo("Kru")
                .region("Latam")
                .build();

        when(equipoRepository.save(any(Equipo.class))).thenReturn(equipoGuardado);

        EquipoResponse resultado = service.crearEquipo(equipo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Kru", resultado.getNombreEquipo());
        assertEquals("Latam", resultado.getRegion());
        verify(equipoRepository).save(any(Equipo.class));
    }

    @Test
    void deberiaAgregarStaffAlEquipo() {
        EquipoRequest equipo = EquipoRequest.builder()
                .nombreEquipo("Leviatan")
                .region("Latam")
                .build();

        Equipo equipoExistente = Equipo.builder()
                .id(1L)
                .nombreEquipo("Kru")
                .region("Latam")
                .build();

        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoExistente));
        when(equipoRepository.save(any(Equipo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EquipoResponse resultado = service.actualizarEquipo(1L, equipo);

        assertEquals(1L, resultado.getId());
        assertEquals("Leviatan", resultado.getNombreEquipo());
        assertEquals("Latam", resultado.getRegion());
        verify(equipoRepository).findById(1L);
    }

    @Test
    void deberiaAgregarJugadoresAlEquipo() {
        Long equipoId = 1L;

        String token = "Token";

        Equipo equipoGuardado = Equipo.builder()
                .id(1L)
                .nombreEquipo("SKT T1")
                .region("COREA")
                .build();

        RosterHistoricoRequest jugadorNuevo = RosterHistoricoRequest.builder()
                .idJugador(2L)
                .fechaInicio(LocalDate.parse("1998-07-08"))
                .build();

        JugadorResponse jugadorRespuesta = JugadorResponse.builder()
                .id(2L)
                .nickname("Faker")
                .build();

        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoGuardado));
        when(jugadorClient.obtenerJugador(2L, token)).thenReturn(jugadorRespuesta);

        EquipoResponse resultado = service.agregarJugador(equipoId, jugadorNuevo, token);

        assertNotNull(resultado);
        assertEquals("SKT T1", resultado.getNombreEquipo());
        assertEquals(1, resultado.getCantidadRoster());
        verify(equipoRepository).findById(equipoId);
        verify(jugadorClient).obtenerJugador(2L, token);
        verify(rosterHistoricoRepository).save(any(RosterHistorico.class));
    }

    @Test
    void deberiaRetornarUnaListaDeJugadores() {
        Equipo equipoGuardado = Equipo.builder()
                .id(1L)
                .nombreEquipo("SKT T1")
                .region("COREA")
                .build();

        when(equipoRepository.findAll()).thenReturn(List.of(equipoGuardado));

        List<EquipoResponse> resultado = service.listarEquipos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("SKT T1", resultado.get(0).getNombreEquipo());
        assertEquals("COREA", resultado.get(0).getRegion());
        verify(equipoRepository).findAll();
    }

    @Test
    void deberiaListarEquiposPorRegion() {
        Equipo equipoGuardado = Equipo.builder()
                .id(1L)
                .nombreEquipo("KRU")
                .region("LATAM")
                .build();

        when(equipoRepository.findByRegion("LATAM")).thenReturn(List.of(equipoGuardado));

        List<EquipoResponse> resultado = service.listarEquiposPorRegion("LATAM");

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("KRU", resultado.get(0).getNombreEquipo());
        assertEquals("LATAM", resultado.get(0).getRegion());
        verify(equipoRepository).findByRegion("LATAM");
    }

    @Test
    void deberiaActualizarAlEquipo() {
        Equipo equipoGuardado = Equipo.builder()
                .id(1L)
                .nombreEquipo("KLG")
                .region("LATAM")
                .build();

        EquipoRequest equipoActualizar = EquipoRequest.builder()
                .nombreEquipo("ISG")
                .region("LATAM")
                .build();

        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoGuardado));
        when(equipoRepository.save(any(Equipo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EquipoResponse resultado = service.actualizarEquipo(1L, equipoActualizar);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("ISG", resultado.getNombreEquipo());
        assertEquals("LATAM", resultado.getRegion());
        verify(equipoRepository).findById(1L);
        verify(equipoRepository).save(any(Equipo.class));
    }

    @Test
    void deberiaDesactivarEquipo() {
        Equipo equipoGuardado = Equipo.builder()
                .id(1L)
                .nombreEquipo("KLG")
                .region("LATAM")
                .activo(true)
                .build();

        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoGuardado));

        service.desactivarEquipo(1L);

        assertFalse(equipoGuardado.getActivo());
        verify(equipoRepository).findById(1L);
        verify(equipoRepository).save(equipoGuardado);
    }

    @Test
    void deberiaTerminarContratoJugador() {
        RosterHistorico rosterGuardado = RosterHistorico.builder()
                .id(1L)
                .fechaFin(null)
                .build();

        when(rosterHistoricoRepository.findById(1L)).thenReturn(Optional.of(rosterGuardado));

        service.terminarContrato(1L);

        assertNotNull(rosterGuardado.getFechaFin());
        verify(rosterHistoricoRepository).findById(1L);
        verify(rosterHistoricoRepository).save(rosterGuardado);
    }

    @Test
    void deberiaDesactivarAlStaff() {
        StaffTecnico staffGuardado = StaffTecnico.builder()
                .id(1L)
                .activo(true)
                .build();
        when(staffTecnicoRepository.findById(1L)).thenReturn(Optional.of(staffGuardado));

        service.despedirStaff(1L);

        assertFalse(staffGuardado.getActivo());
        verify(staffTecnicoRepository).findById(1L);
        verify(staffTecnicoRepository).save(staffGuardado);
    }

    // TEST DE ERRORES

    @Test
    void deberiaLanzarErrorSiExisteNombreEnLaRegion() {
        EquipoRequest equipoNuevo = EquipoRequest.builder()
                .nombreEquipo("KLG")
                .region("LATAM")
                .build();

        when(equipoRepository.existsByNombreEquipoAndRegion("KLG", "LATAM")).thenReturn(true);

        NombreYRegionYaExistenException ex = assertThrows(NombreYRegionYaExistenException.class,
                () -> service.crearEquipo(equipoNuevo));

        assertNotNull(ex);
        assertEquals("El nombre KLG ya existe en la region LATAM", ex.getMessage());
        verify(equipoRepository, never()).save(any(Equipo.class));
    }

    @Test
    void deberiaLanzarErrorAlBuscarEquipo() {
        StaffTecnicoRequest staffGuardado = StaffTecnicoRequest.builder()
                .build();

        when(equipoRepository.findById(99L)).thenReturn(Optional.empty());

        EquipoNoEncontradoException ex = assertThrows(EquipoNoEncontradoException.class,
                () -> service.agregarStaff(99L, staffGuardado));

        assertNotNull(ex);
        assertEquals("El equipo con ID: 99 no encontrado", ex.getMessage());
        verify(equipoRepository).findById(99L);
    }

    @Test
    void deberiaLanzarErrorPorAgregarOtroHeadCoach() {
        StaffTecnicoRequest staffPeticion = StaffTecnicoRequest.builder()
                .rol("HEADCOACH")
                .build();
        Equipo equipoSimulado = Equipo.builder()
                .id(1L)
                .nombreEquipo("KLG")
                .region("LATAM")
                .activo(true)
                .build();

        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoSimulado));
        when(staffTecnicoRepository.existsByEquipoIdAndRol(1L, "HEADCOACH")).thenReturn(true);

        YaExisteElHeadCoachException ex = assertThrows(YaExisteElHeadCoachException.class,
                () -> service.agregarStaff(1L, staffPeticion));

        assertNotNull(ex);
        assertEquals("Ya existe un Staff con el rol HEADCOACH", ex.getMessage());
        verify(staffTecnicoRepository).existsByEquipoIdAndRol(1L, "HEADCOACH");
    }

    @Test
    void deberiaLanzarQueElJugadorNoExiste() {
        Equipo equipoSimulado = Equipo.builder()
                .id(1L)
                .nombreEquipo("KLG")
                .region("LATAM")
                .activo(true)
                .build();

        RosterHistoricoRequest jugadorNuevo = RosterHistoricoRequest.builder()
                .idJugador(2L)
                .build();

        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoSimulado));
        when(jugadorClient.obtenerJugador(2L, "token")).thenReturn(null);

        JugadorNoExisteException ex = assertThrows(JugadorNoExisteException.class,
                () -> service.agregarJugador(1L, jugadorNuevo, "token"));

        assertNotNull(ex);
        assertEquals("Jugador con la ID: 2 no encontrado", ex.getMessage());
        verify(jugadorClient).obtenerJugador(2L, "token");
        verify(equipoRepository).findById(1L);
    }

    @Test
    void deberiaLanzarNombreYRegionYaexisten() {
        EquipoRequest equipoNuevo = EquipoRequest.builder()
                .nombreEquipo("FURIUS")
                .region("LATAM")
                .build();

        Equipo equipoGuardado = Equipo.builder()
                .id(1L)
                .nombreEquipo("FURIUS")
                .region("LATAM")
                .build();

        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoGuardado));
        when(equipoRepository.existsByNombreEquipoAndRegionAndIdNot("FURIUS", "LATAM", 1L)).thenReturn(true);

        NombreYRegionYaExistenException ex = assertThrows(NombreYRegionYaExistenException.class,
                () -> service.actualizarEquipo(1L, equipoNuevo));

        assertNotNull(ex);
        assertEquals("Ya existe el nombre de equipo FURIUS dentro de la región LATAM", ex.getMessage());
        verify(equipoRepository).findById(1L);
        verify(equipoRepository).existsByNombreEquipoAndRegionAndIdNot("FURIUS", "LATAM", 1L);
    }

    @Test
    void deberiaLanzarRosterIdNoEncontrado() {
        when(rosterHistoricoRepository.findById(5L)).thenReturn(Optional.empty());

        RosterIdNoEncontradoException ex = assertThrows(RosterIdNoEncontradoException.class,
                () -> service.terminarContrato(5L));

        assertNotNull(ex);
        assertEquals("El roster con ID: 5 no encontrado", ex.getMessage());
        verify(rosterHistoricoRepository).findById(5L);
    }

    @Test
    void deberiaLanzarStaffNoEncontrado() {
        when(staffTecnicoRepository.findById(6L)).thenReturn(Optional.empty());

        StaffNoEncontradoException ex = assertThrows(StaffNoEncontradoException.class, () -> service.despedirStaff(6L));

        assertNotNull(ex);
        assertEquals("El staff con la ID: 6 no encontrado", ex.getMessage());
        verify(staffTecnicoRepository).findById(6L);
    }
}