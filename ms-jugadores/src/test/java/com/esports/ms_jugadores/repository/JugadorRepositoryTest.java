package com.esports.ms_jugadores.repository;

import com.esports.ms_jugadores.model.Jugador;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JugadorRepositoryTest {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Test
    void debeBuscarPorId() {
        Jugador jugador = Jugador.builder()
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("USER")
                .activo(true)
                .salarioMensual(10000.0)
                .build();
        Jugador guardado = jugadorRepository.save(jugador);

        Optional<Jugador> resultado = jugadorRepository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Shufflan", resultado.get().getNickname());
        assertEquals(LocalDate.parse("2000-07-18"), resultado.get().getFechaNacimiento());
    }

    @Test
    void debeRevisarSiExiste() {
        Jugador jugador = Jugador.builder()
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("USER")
                .activo(true)
                .salarioMensual(10000.0)
                .build();
        Jugador guardado = jugadorRepository.save(jugador);

        boolean resultado = jugadorRepository.existsByNickname(guardado.getNickname());

        assertTrue(resultado);
    }

    @Test
    void debeRetornarListaPorPaises() {
        Jugador jugador = Jugador.builder()
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("USER")
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        Jugador jugador2 = Jugador.builder()
                .nickname("nakaseomyces")
                .nombreReal("Perla Valentina")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-05-02"))
                .rol("USER")
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        jugadorRepository.save(jugador);
        jugadorRepository.save(jugador2);

        List<Jugador> resultado = jugadorRepository.findByPais("Chile");

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeRetornarListaPorIdEquipos() {
        Jugador jugador = Jugador.builder()
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("USER")
                .activo(true)
                .idEquipoActual(2L)
                .salarioMensual(10000.0)
                .build();

        Jugador jugador2 = Jugador.builder()
                .nickname("nakaseomyces")
                .nombreReal("Perla Valentina")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-05-02"))
                .rol("USER")
                .activo(true)
                .idEquipoActual(2L)
                .salarioMensual(10000.0)
                .build();

        jugadorRepository.save(jugador);
        jugadorRepository.save(jugador2);

        List<Jugador> resultado = jugadorRepository.findByIdEquipoActual(2L);

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeRetornarJugadorQueEsteActivo() {
        Jugador jugador = Jugador.builder()
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("USER")
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        Jugador jugador2 = Jugador.builder()
                .nickname("nakaseomyces")
                .nombreReal("Perla Valentina")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-05-02"))
                .rol("USER")
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        jugadorRepository.save(jugador);
        jugadorRepository.save(jugador2);

        List<Jugador> resultado = jugadorRepository.findByActivoTrue();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeGuardarjugador() {
        Jugador jugador = Jugador.builder()
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("USER")
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        Jugador resultado = jugadorRepository.save(jugador);

        assertEquals("Shufflan", resultado.getNickname());
    }

    @Test
    void debeListarTodosLosJugadores() {
        Jugador jugador = Jugador.builder()
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("USER")
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        Jugador jugador2 = Jugador.builder()
                .nickname("nakaseomyces")
                .nombreReal("Perla Valentina")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-05-02"))
                .rol("USER")
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        jugadorRepository.save(jugador);
        jugadorRepository.save(jugador2);

        List<Jugador> resultado = jugadorRepository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarUnJugador() {
        Jugador jugador = Jugador.builder()
                .nickname("Shufflan")
                .nombreReal("Camilo Covarrubias")
                .pais("Chile")
                .fechaNacimiento(LocalDate.parse("2000-07-18"))
                .rol("USER")
                .activo(true)
                .salarioMensual(10000.0)
                .build();

        jugadorRepository.save(jugador);
        jugadorRepository.delete(jugador);

        Optional<Jugador> resultado = jugadorRepository.findById(jugador.getId());

        assertFalse(resultado.isPresent());
    }

}
