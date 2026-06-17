package com.esports.ms_estadisticas.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.esports.ms_estadisticas.model.Estadistica;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EstadisticaRepositoryTest {

    @Autowired
    private EstadisticaRepository estadisticaRepository;

    Estadistica estadisticaPrueba = Estadistica.builder()
            .idJugador(1L)
            .idPartida(10L)
            .nicknameJugador("Faker")
            .asesinatos(10)
            .muertes(2)
            .asistencias(6)
            .kda(8.0)
            .mvp(true)
            .fechaRegistro(LocalDateTime.now())
            .build();

    @Test
    void deberiaDevolverUnaListaPorIdJugador() {
        Estadistica estadisticaGuardada = estadisticaRepository.save(estadisticaPrueba);

        List<Estadistica> resultado = estadisticaRepository.findByIdJugador(estadisticaGuardada.getIdJugador());

        assertFalse(resultado.isEmpty());
        assertEquals(1L, resultado.get(0).getIdJugador());
        assertEquals("Faker", resultado.get(0).getNicknameJugador());
    }

    @Test
    void deberiaDevolverUnaListaPorIdPartida() {
        Estadistica estadisticaGuardada = estadisticaRepository.save(estadisticaPrueba);

        List<Estadistica> resultado = estadisticaRepository.findByIdPartida(estadisticaGuardada.getIdPartida());

        assertFalse(resultado.isEmpty());
        assertEquals(10L, resultado.get(0).getIdPartida());
        assertEquals("Faker", resultado.get(0).getNicknameJugador());
    }

    @Test
    void deberiaDevolverTrueSiExistePorIdJugadorEIdPartida() {
        Estadistica estadisticaGuardada = estadisticaRepository.save(estadisticaPrueba);

        Boolean resultado = estadisticaRepository.existsByIdJugadorAndIdPartida(
                estadisticaGuardada.getIdJugador(),
                estadisticaGuardada.getIdPartida());

        assertTrue(resultado);
    }
}