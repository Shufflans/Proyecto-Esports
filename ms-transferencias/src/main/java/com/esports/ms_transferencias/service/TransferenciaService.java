package com.esports.ms_transferencias.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.esports.ms_transferencias.client.EquipoClient;
import com.esports.ms_transferencias.client.JugadorClient;
import com.esports.ms_transferencias.dto.EquipoResponse;
import com.esports.ms_transferencias.dto.JugadorResponse;
import com.esports.ms_transferencias.dto.TransferenciaRequest;
import com.esports.ms_transferencias.dto.TransferenciaResponse;
import com.esports.ms_transferencias.exception.EquipoNoEncontradoException;
import com.esports.ms_transferencias.exception.JugadorNoExisteException;
import com.esports.ms_transferencias.exception.TipoDeTransferenciaInvalidoException;
import com.esports.ms_transferencias.model.Transferencia;
import com.esports.ms_transferencias.repository.TransferenciaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final JugadorClient jugadorClient;
    private final EquipoClient equipoClient;

    @Transactional
    public TransferenciaResponse registrarTransferencia(TransferenciaRequest tr, String token) {
        log.info("Registrando transferencia para el jugador ID: {}", tr.getIdJugador());

        JugadorResponse jugadorRemoto = jugadorClient.obtenerJugador(tr.getIdJugador(), token);
        if (jugadorRemoto == null) {
            log.warn("El jugador con ID: {} no existe", tr.getIdJugador());
            throw new JugadorNoExisteException("Jugador con la ID: " + tr.getIdJugador() + " no encontrado");
        }

        String nombreOrigen = null;
        String nombreDestino = null;

        switch (tr.getTipo().toUpperCase()) {
            case "FICHAJE_INICIAL":
                if (tr.getIdEquipoDestino() == null) {
                    throw new TipoDeTransferenciaInvalidoException("Un fichaje inicial requiere un equipo destino.");
                }
                EquipoResponse eqDestino = equipoClient.obtenerEquipo(tr.getIdEquipoDestino(), token);
                if (eqDestino == null) {
                    log.warn("El equipo destino con ID: {} no existe", tr.getIdEquipoDestino());
                    throw new EquipoNoEncontradoException(
                            "El equipo con ID: " + tr.getIdEquipoDestino() + " no encontrado");
                }
                nombreDestino = eqDestino.getNombreEquipo();
                break;

            case "BAJA":
                if (tr.getIdEquipoOrigen() == null) {
                    throw new TipoDeTransferenciaInvalidoException("Una baja requiere un equipo origen.");
                }
                EquipoResponse eqOrigen = equipoClient.obtenerEquipo(tr.getIdEquipoOrigen(), token);
                if (eqOrigen == null) {
                    log.warn("El equipo origen con ID: {} no existe", tr.getIdEquipoOrigen());
                    throw new EquipoNoEncontradoException(
                            "El equipo con ID: " + tr.getIdEquipoOrigen() + " no encontrado");
                }
                nombreOrigen = eqOrigen.getNombreEquipo();
                break;

            case "TRANSFERENCIA":
            case "PRESTAMO":
                if (tr.getIdEquipoOrigen() == null || tr.getIdEquipoDestino() == null) {
                    throw new TipoDeTransferenciaInvalidoException(
                            "Este tipo de movimiento requiere equipo origen y destino.");
                }
                EquipoResponse orig = equipoClient.obtenerEquipo(tr.getIdEquipoOrigen(), token);
                EquipoResponse dest = equipoClient.obtenerEquipo(tr.getIdEquipoDestino(), token);

                if (orig == null) {
                    log.warn("El equipo origen con ID: {} no existe", tr.getIdEquipoOrigen());
                    throw new EquipoNoEncontradoException(
                            "El equipo con ID: " + tr.getIdEquipoOrigen() + " no encontrado");
                }
                if (dest == null) {
                    log.warn("El equipo destino con ID: {} no existe", tr.getIdEquipoDestino());
                    throw new EquipoNoEncontradoException(
                            "El equipo con ID: " + tr.getIdEquipoDestino() + " no encontrado");
                }
                nombreOrigen = orig.getNombreEquipo();
                nombreDestino = dest.getNombreEquipo();
                break;

            default:
                log.warn("El tipo de transferencia: {} ingresado es inválido", tr.getTipo());
                throw new TipoDeTransferenciaInvalidoException(
                        "El tipo " + tr.getTipo() + " no es un tipo de transferencia válido.");
        }

        Transferencia transferenciaNueva = new Transferencia();

        transferenciaNueva.setIdJugador(tr.getIdJugador());
        transferenciaNueva.setNicknameJugador(jugadorRemoto.getNickname());
        transferenciaNueva.setIdEquipoOrigen(tr.getIdEquipoOrigen());
        transferenciaNueva.setNombreEquipoOrigen(nombreOrigen);
        transferenciaNueva.setIdEquipoDestino(tr.getIdEquipoDestino());
        transferenciaNueva.setNombreEquipoDestino(nombreDestino);
        transferenciaNueva.setFechaTransferencia(tr.getFechaTransferencia());
        transferenciaNueva.setMontoUsd(tr.getMontoUsd());
        transferenciaNueva.setTipo(tr.getTipo().toUpperCase());
        transferenciaNueva.setDuracionContratoMeses(tr.getDuracionContratoMeses());
        transferenciaNueva.setObservaciones(tr.getObservaciones());

        transferenciaRepository.save(transferenciaNueva);

        return mapearATransferencia(transferenciaNueva);
    }

    @Transactional
    public List<TransferenciaResponse> listarTodasLasTransferencias() {
        log.info("Listando todas las transferencias");

        List<TransferenciaResponse> listaTransferencias = transferenciaRepository.findAll().stream()
                .map(this::mapearATransferencia)
                .toList();

        return listaTransferencias;
    }

    @Transactional
    public List<TransferenciaResponse> buscarTransferenciasPorJugador(Long idJugador) {
        log.info("Listando transferencias del jugador con ID: {}", idJugador);

        List<TransferenciaResponse> listaPorJugador = transferenciaRepository.findByIdJugador(idJugador).stream()
                .map(this::mapearATransferencia)
                .toList();

        return listaPorJugador;
    }

    @Transactional
    public List<TransferenciaResponse> buscarTransferenciasPorTipo(String tipo) {
        log.info("Listando transferencias por su tipo: {}", tipo);

        List<TransferenciaResponse> listaPorTipo = transferenciaRepository.findByTipo(tipo.toUpperCase()).stream()
                .map(this::mapearATransferencia)
                .toList();

        return listaPorTipo;
    }

    private TransferenciaResponse mapearATransferencia(Transferencia t) {
        return TransferenciaResponse.builder()
                .id(t.getId())
                .idJugador(t.getIdJugador())
                .nicknameJugador(t.getNicknameJugador())
                .idEquipoOrigen(t.getIdEquipoOrigen())
                .nombreEquipoOrigen(t.getNombreEquipoOrigen())
                .idEquipoDestino(t.getIdEquipoDestino())
                .nombreEquipoDestino(t.getNombreEquipoDestino())
                .fechaTransferencia(t.getFechaTransferencia())
                .montoUsd(t.getMontoUsd())
                .tipo(t.getTipo())
                .duracionContratoMeses(t.getDuracionContratoMeses())
                .observaciones(t.getObservaciones())
                .build();
    }
}