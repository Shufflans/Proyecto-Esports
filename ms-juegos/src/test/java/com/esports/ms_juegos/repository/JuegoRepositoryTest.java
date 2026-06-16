package com.esports.ms_juegos.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.esports.ms_juegos.model.Juego;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JuegoRepositoryTest {

    @Autowired
    private JuegoRepository juegoRepository;

    @Test
    void debeBuscarPorId() {
        Juego juego = Juego.builder()
                .nombreJuego("League of Legends")
                .generoJuego("MOBA")
                .totalPremio(50000.0)
                .activo(true)
                .build();
        Juego guardado = juegoRepository.save(juego);

        Optional<Juego> resultado = juegoRepository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("League of Legends", resultado.get().getNombreJuego());
        assertEquals("MOBA", resultado.get().getGeneroJuego());
    }

    @Test
    void debeRevisarSiExiste() {
        Juego juego = Juego.builder()
                .nombreJuego("Dota 2")
                .generoJuego("MOBA")
                .totalPremio(100000.0)
                .activo(true)
                .build();
        Juego guardado = juegoRepository.save(juego);

        boolean resultado = juegoRepository.existsByNombreJuego(guardado.getNombreJuego());

        assertTrue(resultado);
    }

    @Test
    void debeRetornarListaPorGenero() {
        Juego juego = Juego.builder()
                .nombreJuego("Counter Strike 2")
                .generoJuego("Shooter")
                .totalPremio(40000.0)
                .activo(true)
                .build();

        Juego juego2 = Juego.builder()
                .nombreJuego("Overwatch 2")
                .generoJuego("Shooter")
                .totalPremio(20000.0)
                .activo(true)
                .build();

        juegoRepository.save(juego);
        juegoRepository.save(juego2);

        List<Juego> resultado = juegoRepository.findByGeneroJuego("Shooter");

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeRetornarJuegoQueEsteActivo() {
        Juego juego = Juego.builder()
                .nombreJuego("Rocket League")
                .generoJuego("Deportes")
                .totalPremio(15000.0)
                .activo(true)
                .build();

        Juego juego2 = Juego.builder()
                .nombreJuego("FIFA 24")
                .generoJuego("Deportes")
                .totalPremio(10000.0)
                .activo(true)
                .build();

        juegoRepository.save(juego);
        juegoRepository.save(juego2);

        List<Juego> resultado = juegoRepository.findByActivoTrue();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeGuardarJuego() {
        Juego juego = Juego.builder()
                .nombreJuego("Street Fighter 6")
                .generoJuego("Peleas")
                .totalPremio(12000.0)
                .activo(true)
                .build();

        Juego resultado = juegoRepository.save(juego);

        assertEquals("Street Fighter 6", resultado.getNombreJuego());
        assertNotNull(resultado.getId());
    }

    @Test
    void debeListarTodosLosJuegos() {
        Juego juego = Juego.builder()
                .nombreJuego("Tekken 8")
                .generoJuego("Peleas")
                .totalPremio(15000.0)
                .activo(true)
                .build();

        Juego juego2 = Juego.builder()
                .nombreJuego("Mortal Kombat 1")
                .generoJuego("Peleas")
                .totalPremio(18000.0)
                .activo(true)
                .build();

        juegoRepository.save(juego);
        juegoRepository.save(juego2);

        List<Juego> resultado = juegoRepository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarUnJuego() {
        Juego juego = Juego.builder()
                .nombreJuego("Smite")
                .generoJuego("MOBA")
                .totalPremio(8000.0)
                .activo(true)
                .build();

        Juego guardado = juegoRepository.save(juego);
        juegoRepository.delete(guardado);

        Optional<Juego> resultado = juegoRepository.findById(guardado.getId());

        assertFalse(resultado.isPresent());
    }
}
