package com.esports.ms_juegos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.esports.ms_juegos.dto.JuegoRequest;
import com.esports.ms_juegos.dto.JuegoResponse;
import com.esports.ms_juegos.exception.JuegoNoEncontradoException;
import com.esports.ms_juegos.exception.NombreDeJuegoExisteException;
import com.esports.ms_juegos.model.Juego;
import com.esports.ms_juegos.repository.JuegoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JuegoService {

    private final JuegoRepository juegoRepository;

    @Transactional
    public JuegoResponse crearJuego(JuegoRequest jr) {
        log.info("Creando juego: {}", jr.getNombreJuego());

        if (juegoRepository.existsByNombreJuego(jr.getNombreJuego())) {
            log.warn("El juego {} ingresado ya existe", jr.getNombreJuego());
            throw new NombreDeJuegoExisteException("El Juego " + jr.getNombreJuego() + " ya existe.");
        }

        Juego juegoNuevo = new Juego();

        juegoNuevo.setNombreJuego(jr.getNombreJuego());
        juegoNuevo.setGeneroJuego(jr.getGeneroJuego());
        juegoNuevo.setActivo(true);

        juegoRepository.save(juegoNuevo);

        return mapearAJuego(juegoNuevo);
    }

    @Transactional
    public JuegoResponse buscarJuegoPorId(Long id) {
        log.info("Buscando juego por ID: {}", id);

        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("El Juego con la ID: {} no encontrado", id);
                    return new JuegoNoEncontradoException("Juego con la ID: " + id + " no encontrado");
                });

        return mapearAJuego(juego);
    }

    @Transactional
    public List<JuegoResponse> listarTodosLosJuegos() {
        log.info("Listando todos los juegos");

        List<JuegoResponse> listaJuegos = juegoRepository.findAll().stream()
                .map(this::mapearAJuego)
                .toList();

        return listaJuegos;
    }

    @Transactional
    public List<JuegoResponse> listarJuegosActivos() {
        log.info("Listar todos los juegos activos");

        List<JuegoResponse> listaJuegosActivos = juegoRepository.findByActivoTrue().stream()
                .map(this::mapearAJuego)
                .toList();

        return listaJuegosActivos;
    }

    @Transactional
    public List<JuegoResponse> buscarJuegoPorGenero(String genero) {
        log.info("Listando Juegos por su género");

        List<JuegoResponse> listaJuegosGenero = juegoRepository.findByGeneroJuego(genero).stream()
                .map(this::mapearAJuego).toList();

        return listaJuegosGenero;
    }

    @Transactional
    public JuegoResponse actualizarJuego(Long id, JuegoRequest jr) {
        log.info("Actualizando el juego: {} | ID: {}", jr.getNombreJuego(), id);

        Juego juegoBuscado = juegoRepository.findById(id).orElseThrow(() -> {
            log.warn("El Juego con la ID: {} no encontrado", id);
            return new JuegoNoEncontradoException("Juego con la ID: " + id + " no encontrado");
        });

        if (!jr.getNombreJuego().equalsIgnoreCase(juegoBuscado.getNombreJuego())) {
            if (juegoRepository.existsByNombreJuego(jr.getNombreJuego())) {
                log.warn("El nombre: {} ya existe", juegoBuscado.getNombreJuego());
                throw new NombreDeJuegoExisteException("El Juego " + jr.getNombreJuego() + " ya existe.");
            }
        }

        juegoBuscado.setNombreJuego(jr.getNombreJuego());
        juegoBuscado.setGeneroJuego(jr.getGeneroJuego());

        juegoRepository.save(juegoBuscado);

        return mapearAJuego(juegoBuscado);
    }

    @Transactional
    public void desactivarJuego(Long id) {
        log.info("Desactivando el juego con ID: {}", id);

        Juego juegoEncontrado = juegoRepository.findById(id).orElseThrow(() -> {
            log.warn("El Juego con la ID: {} no encontrado", id);
            return new JuegoNoEncontradoException("Juego con la ID: " + id + " no encontrado");
        });

        juegoEncontrado.setActivo(false);
        juegoRepository.save(juegoEncontrado);
    }

    private JuegoResponse mapearAJuego(Juego j) {
        return JuegoResponse.builder()
                .id(j.getId())
                .nombreJuego(j.getNombreJuego())
                .generoJuego(j.getGeneroJuego())
                .activo(j.getActivo())
                .build();
    }

}
