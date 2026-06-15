package com.esports.ms_equipos.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

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
import com.esports.ms_equipos.repository.RosterHistoricoRepository;
import com.esports.ms_equipos.repository.EquipoRepository;
import com.esports.ms_equipos.repository.StaffTecnicoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final RosterHistoricoRepository rosterHistoricoRepository;
    private final StaffTecnicoRepository staffTecnicoRepository;
    private final JugadorClient jugadorClient;

    @Transactional
    public EquipoResponse crearEquipo(EquipoRequest er) {
        log.info("Creando equipo: {}", er.getNombreEquipo());

        if (equipoRepository.existsByNombreEquipoAndRegion(er.getNombreEquipo(), er.getRegion())) {
            log.warn("El equipo: {} ya existe en la región: {}", er.getNombreEquipo(), er.getRegion());
            throw new NombreYRegionYaExistenException(
                    "El nombre " + er.getNombreEquipo() + " ya existe en la region " + er.getRegion());
        }

        Equipo crearEquipo = Equipo.builder()
                .nombreEquipo(er.getNombreEquipo())
                .region(er.getRegion())
                .fechaFundacion(er.getFechaFundacion())
                .rankingMundial(null)
                .activo(true)
                .build();

        Equipo equipoGuardado = equipoRepository.save(crearEquipo);

        return mapearAEquipoResponse(equipoGuardado);
    }

    @Transactional
    public EquipoResponse agregarStaff(Long equipoId, StaffTecnicoRequest st) {
        log.info("Agregando Staff: {} con rol:{}", st.getNombreStaff(), st.getRol());

        Equipo equipoRecuperado = equipoRepository.findById(equipoId)
                .orElseThrow(
                        () -> new EquipoNoEncontradoException("El equipo con ID: " + equipoId + " no encontrado"));

        if (st.getRol().equalsIgnoreCase("HEADCOACH")) {
            Boolean existe = staffTecnicoRepository.existsByEquipoIdAndRol(equipoId, "HEADCOACH");
            if (existe) {
                log.warn("Ya existe un staff con el rol: HEADCOACH");
                throw new YaExisteElHeadCoachException("Ya existe un Staff con el rol HEADCOACH");
            }
        }
        StaffTecnico staffcreado = StaffTecnico.builder()
                .nombreStaff(st.getNombreStaff())
                .rol(st.getRol())
                .salarioMensual(st.getSalarioMensual())
                .activo(true)
                .equipo(equipoRecuperado)
                .build();

        equipoRecuperado.agregarStaff(staffcreado);
        staffTecnicoRepository.save(staffcreado);

        return mapearAEquipoResponse(equipoRecuperado);
    }

    @Transactional
    public EquipoResponse agregarJugador(Long equipoId, RosterHistoricoRequest rh, String token) {
        log.info("Agregando Jugador: {} | al equipo: {}", rh.getIdJugador(), equipoId);

        Equipo equipoRecuperado = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new EquipoNoEncontradoException("El equipo con ID: " + equipoId + " no encontrado"));

        JugadorResponse jugador = jugadorClient.obtenerJugador(rh.getIdJugador(), token);

        if (jugador == null) {
            log.warn("El jugador con ID: {} no existe", rh.getIdJugador());
            throw new JugadorNoExisteException("Jugador con la ID: " + rh.getIdJugador() + " no encontrado");
        }

        RosterHistorico crearRoster = RosterHistorico.builder()
                .idJugador(jugador.getId())
                .nickname(jugador.getNickname())
                .fechaInicio(rh.getFechaInicio())
                .fechaFin(rh.getFechaFin())
                .equipo(equipoRecuperado)
                .build();

        equipoRecuperado.agregarRoster(crearRoster);
        rosterHistoricoRepository.save(crearRoster);

        return mapearAEquipoResponse(equipoRecuperado);
    }

    @Transactional
    public List<EquipoResponse> listarEquipos() {
        log.info("Listando todos los equipos.");
        List<EquipoResponse> equiposListados = equipoRepository.findAll().stream()
                .map(this::mapearAEquipoResponse)
                .toList();
        return equiposListados;
    }

    @Transactional
    public List<EquipoResponse> listarEquiposPorRegion(String region) {
        log.info("Listando los equipos por region: {}", region);
        List<EquipoResponse> equiposPorRegion = equipoRepository.findByRegion(region).stream()
                .map(this::mapearAEquipoResponse)
                .toList();
        return equiposPorRegion;
    }

    @Transactional
    public EquipoResponse actualizarEquipo(Long equipoId, EquipoRequest er) {
        log.info("Actualizando al equipo con ID: {} | nombre: {}", equipoId, er.getNombreEquipo());

        Equipo equipoEncontrado = equipoRepository.findById(equipoId).orElseThrow(
                () -> new EquipoNoEncontradoException("El equipo con ID: " + equipoId + " no ha sido encontrado"));

        if (equipoRepository.existsByNombreEquipoAndRegionAndIdNot(er.getNombreEquipo(), er.getRegion(), equipoId)) {
            log.warn("El nombre del equipo: {} ya existe dentro de la región: {}", er.getNombreEquipo(),
                    er.getRegion());
            throw new NombreYRegionYaExistenException("Ya existe el nombre de equipo "
                    + er.getNombreEquipo() + " dentro de la región " + er.getRegion());
        }

        equipoEncontrado.setNombreEquipo(er.getNombreEquipo());
        equipoEncontrado.setRegion(er.getRegion());
        equipoEncontrado.setFechaFundacion(er.getFechaFundacion());
        equipoEncontrado.setRankingMundial(er.getRankingMundial());

        equipoRepository.save(equipoEncontrado);

        return mapearAEquipoResponse(equipoEncontrado);
    }

    @Transactional
    public void desactivarEquipo(Long id) {
        log.info("Desactivando al equipo de ID: {}", id);

        Equipo equipoEncontrado = equipoRepository.findById(id).orElseThrow(
                () -> new EquipoNoEncontradoException("El equipo con ID: " + id + " no encontrado"));

        equipoEncontrado.setActivo(false);
        equipoRepository.save(equipoEncontrado);
    }

    @Transactional
    public void terminarContrato(Long rosterId) {
        log.info("Terminando contrato del roster con ID: {}", rosterId);

        RosterHistorico rosterRecuperado = rosterHistoricoRepository.findById(rosterId).orElseThrow(
                () -> new RosterIdNoEncontradoException("El roster con ID: " + rosterId + " no encontrado"));

        rosterRecuperado.setFechaFin(LocalDate.now());
        rosterHistoricoRepository.save(rosterRecuperado);
    }

    @Transactional
    public void despedirStaff(Long staffId) {
        log.info("Desactivando a staff con ID: {}", staffId);

        StaffTecnico staffRecuperado = staffTecnicoRepository.findById(staffId)
                .orElseThrow(() -> new StaffNoEncontradoException("El staff con la ID: " + staffId + " no encontrado"));

        staffRecuperado.setActivo(false);
        staffTecnicoRepository.save(staffRecuperado);
    }

    private EquipoResponse mapearAEquipoResponse(Equipo e) {
        return EquipoResponse.builder()
                .id(e.getId())
                .nombreEquipo(e.getNombreEquipo())
                .region(e.getRegion())
                .fechaFundacion(e.getFechaFundacion())
                .rankingMundial(e.getRankingMundial())
                .activo(e.getActivo())
                .cantidadStaff(e.getStaff().size())
                .cantidadRoster(e.getRosterHistorico().size())
                .build();
    }

}
