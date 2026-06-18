package com.esports.ms_rankings.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.esports.ms_rankings.model.Ranking;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RankingRepositoryTest {

        @Autowired
        private RankingRepository rankingRepository;

        @Test
        void debeBuscarPorId() {
                Ranking ranking = Ranking.builder()
                                .idJugador(1L)
                                .nombreJugador("Faker")
                                .idEquipo(10L)
                                .nombreEquipo("T1")
                                .puntos(1500)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                Ranking guardado = rankingRepository.save(ranking);

                Optional<Ranking> resultado = rankingRepository.findById(guardado.getId());

                assertTrue(resultado.isPresent());
                assertEquals(1L, resultado.get().getIdJugador());
                assertEquals("Faker", resultado.get().getNombreJugador());
        }

        @Test
        void debeRetornarListaPorJugador() {
                Ranking ranking1 = Ranking.builder()
                                .idJugador(5L)
                                .nombreJugador("ShowMaker")
                                .idEquipo(20L)
                                .nombreEquipo("Dplus KIA")
                                .puntos(1000)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                Ranking ranking2 = Ranking.builder()
                                .idJugador(5L)
                                .nombreJugador("ShowMaker")
                                .idEquipo(30L)
                                .nombreEquipo("Dplus KIA Academy")
                                .puntos(1200)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                rankingRepository.save(ranking1);
                rankingRepository.save(ranking2);

                List<Ranking> resultado = rankingRepository.findByIdJugador(5L);

                assertFalse(resultado.isEmpty());
                assertTrue(resultado.size() >= 2);
        }

        @Test
        void debeRetornarListaPorEquipo() {
                Ranking ranking1 = Ranking.builder()
                                .idJugador(1L)
                                .nombreJugador("Rekkles")
                                .idEquipo(15L)
                                .nombreEquipo("Fnatic")
                                .puntos(1000)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                Ranking ranking2 = Ranking.builder()
                                .idJugador(2L)
                                .nombreJugador("Humanoid")
                                .idEquipo(15L)
                                .nombreEquipo("Fnatic")
                                .puntos(1100)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                rankingRepository.save(ranking1);
                rankingRepository.save(ranking2);

                List<Ranking> resultado = rankingRepository.findByIdEquipo(15L);

                assertFalse(resultado.isEmpty());
                assertTrue(resultado.size() >= 2);
        }

        @Test
        void debeGuardarRanking() {
                Ranking ranking = Ranking.builder()
                                .idJugador(10L)
                                .nombreJugador("Chovy")
                                .idEquipo(25L)
                                .nombreEquipo("Gen.G")
                                .puntos(2000)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                Ranking resultado = rankingRepository.save(ranking);

                assertEquals("Chovy", resultado.getNombreJugador());
                assertNotNull(resultado.getId());
        }

        @Test
        void debeListarTodosLosRankings() {
                Ranking ranking1 = Ranking.builder()
                                .idJugador(1L)
                                .nombreJugador("Caps")
                                .idEquipo(10L)
                                .nombreEquipo("G2 Esports")
                                .puntos(500)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                Ranking ranking2 = Ranking.builder()
                                .idJugador(2L)
                                .nombreJugador("Mikyx")
                                .idEquipo(20L)
                                .nombreEquipo("G2 Esports")
                                .puntos(600)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                rankingRepository.save(ranking1);
                rankingRepository.save(ranking2);

                List<Ranking> resultado = rankingRepository.findAll();

                assertFalse(resultado.isEmpty());
                assertTrue(resultado.size() >= 2);
        }

        @Test
        void debeEliminarUnRanking() {
                Ranking ranking = Ranking.builder()
                                .idJugador(1L)
                                .nombreJugador("Oner")
                                .idEquipo(10L)
                                .nombreEquipo("T1")
                                .puntos(500)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                Ranking guardado = rankingRepository.save(ranking);

                rankingRepository.delete(guardado);

                Optional<Ranking> resultado = rankingRepository.findById(guardado.getId());

                assertFalse(resultado.isPresent());
        }
}