package com.esports.ms_equipos.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.esports.ms_equipos.model.Equipo;
import com.esports.ms_equipos.model.RosterHistorico;
import com.esports.ms_equipos.model.StaffTecnico;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EquipoRepositoryTest {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private RosterHistoricoRepository rosterHistoricoRepository;

    @Autowired
    private StaffTecnicoRepository staffTecnicoRepository;

    Equipo equipoPrueba = Equipo.builder()
            .nombreEquipo("KRU")
            .region("LATAM")
            .fechaFundacion(LocalDate.parse("2015-07-09"))
            .rankingMundial(0)
            .activo(true)
            .build();

    RosterHistorico rosterPrueba = RosterHistorico.builder()
            .idJugador(2L)
            .nickname("Miguel")
            .fechaInicio(LocalDate.parse("2020-09-10"))
            .fechaFin(null)
            .equipo(equipoPrueba)
            .build();

    StaffTecnico staffPrueba = StaffTecnico.builder()
            .nombreStaff("Coach Silva")
            .rol("COACH")
            .activo(true)
            .salarioMensual(20000.0)
            .equipo(equipoPrueba)
            .build();

    @Test
    void deberiaBuscarPorElEquipoYRegion() {
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba);
        Optional<Equipo> guardado = equipoRepository.findByNombreEquipoAndRegion(equipoGuardado.getNombreEquipo(),
                equipoGuardado.getRegion());

        assertTrue(guardado.isPresent());
        assertEquals("KRU", guardado.get().getNombreEquipo());
        assertEquals("LATAM", guardado.get().getRegion());
    }

    @Test
    void deberiaDevolverTrueSiExisteNombreYRegion() {
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba);

        Boolean resultado = equipoRepository.existsByNombreEquipoAndRegion(equipoGuardado.getNombreEquipo(),
                equipoGuardado.getRegion());

        assertTrue(resultado);
    }

    @Test
    void deberiaDevolverUnaListaPorRegion() {
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba);

        List<Equipo> resultado = equipoRepository.findByRegion(equipoGuardado.getRegion());

        assertFalse(resultado.isEmpty());
        assertEquals("LATAM", resultado.get(0).getRegion());
    }

    @Test
    void deberiaDevolverUnaListaSiElJugadorEstaActivo() {
        equipoRepository.save(equipoPrueba);

        List<Equipo> resultado = equipoRepository.findByActivoTrue();

        assertFalse(resultado.isEmpty());
        assertEquals(true, resultado.get(0).getActivo());
    }

    @Test
    void deberiaDevolverEquipoPorId() {
        Equipo equipoPrueba2 = Equipo.builder()
                .nombreEquipo("KRU")
                .region("LATAM")
                .fechaFundacion(LocalDate.parse("2015-07-09"))
                .rankingMundial(0)
                .activo(true)
                .build();
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba2);

        Optional<Equipo> resultado = equipoRepository.findById(equipoPrueba2.getId());

        assertTrue(resultado.isPresent());
        assertEquals(equipoGuardado.getId(), resultado.get().getId());
    }

    @Test
    void debeDevolverTrueSiExisteNombreRegionEId() {
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba);

        Boolean resultado = equipoRepository.existsByNombreEquipoAndRegionAndIdNot(equipoGuardado.getNombreEquipo(),
                equipoGuardado.getRegion(), equipoGuardado.getId());

        assertFalse(resultado);
    }

    @Test
    void devolverUnaListaDePorLaIdDelEquipo() {
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba);

        rosterPrueba.setEquipo(equipoGuardado);
        rosterHistoricoRepository.save(rosterPrueba);

        List<RosterHistorico> resultado = rosterHistoricoRepository.findByEquipoId(rosterPrueba.getEquipo().getId());

        assertFalse(resultado.isEmpty());
        assertEquals(equipoGuardado.getId(), resultado.get(0).getEquipo().getId());
    }

    @Test
    void deberiaDevolverRosterActivosDondeFechaFinIsNull() {
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba);

        rosterPrueba.setEquipo(equipoGuardado);
        rosterHistoricoRepository.save(rosterPrueba);

        List<RosterHistorico> resultado = rosterHistoricoRepository
                .findByEquipoIdAndFechaFinIsNull(rosterPrueba.getEquipo().getId());

        assertFalse(resultado.isEmpty());
        assertNull(resultado.get(0).getFechaFin());
    }

    @Test
    void buscarRosterPorIdDeJugador() {
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba);

        rosterPrueba.setEquipo(equipoGuardado);
        rosterHistoricoRepository.save(rosterPrueba);

        List<RosterHistorico> resultado = rosterHistoricoRepository.findByIdJugador(rosterPrueba.getIdJugador());

        assertFalse(resultado.isEmpty());
        assertEquals(2L, resultado.get(0).getIdJugador());
    }

    @Test
    void deberiaDevolverUnaListaDeStaffPorIdDelEquipo() {
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba);

        staffPrueba.setEquipo(equipoGuardado);
        staffTecnicoRepository.save(staffPrueba);

        List<StaffTecnico> resultado = staffTecnicoRepository.findByEquipoId(staffPrueba.getEquipo().getId());

        assertFalse(resultado.isEmpty());
        assertEquals(equipoGuardado.getId(), resultado.get(0).getEquipo().getId());
    }

    @Test
    void deberiaDevolverStaffPorEquipoRolYActivo() {
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba);

        staffPrueba.setEquipo(equipoGuardado);
        staffTecnicoRepository.save(staffPrueba);

        List<StaffTecnico> resultado = staffTecnicoRepository.findByEquipoIdAndRolAndActivoTrue(equipoGuardado.getId(),
                staffPrueba.getRol());

        assertFalse(resultado.isEmpty());
        assertEquals(equipoGuardado.getId(), resultado.get(0).getEquipo().getId());
        assertEquals("COACH", resultado.get(0).getRol());
    }

    @Test
    void deberiaDevolverTrueSiExisteStaffPorEquipoYRol() {
        Equipo equipoGuardado = equipoRepository.save(equipoPrueba);

        staffPrueba.setEquipo(equipoGuardado);
        staffTecnicoRepository.save(staffPrueba);

        Boolean resultado = staffTecnicoRepository.existsByEquipoIdAndRol(equipoGuardado.getId(), staffPrueba.getRol());

        assertTrue(resultado);
    }
}
