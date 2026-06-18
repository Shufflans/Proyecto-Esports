package com.esports.ms_torneos.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.esports.ms_torneos.model.Torneo;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TorneoRepositoryTest {

        @Autowired
        private TorneoRepository torneoRepository;

        @Test
        void debeBuscarPorId() {
                Torneo torneo = Torneo.builder()
                                .idJuego(1L)
                                .nombreTorneo("Worlds 2025")
                                .nombreJuego("League of Legends")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(2))
                                .totalPremio(2250000.0)
                                .maxEquipos(16)
                                .estado("PROGRAMADO")
                                .build();

                Torneo guardado = torneoRepository.save(torneo);
                Optional<Torneo> resultado = torneoRepository.findById(guardado.getId());

                assertTrue(resultado.isPresent());
                assertEquals("Worlds 2025", resultado.get().getNombreTorneo());
                assertEquals("League of Legends", resultado.get().getNombreJuego());
        }

        @Test
        void debeRetornarListaPorIdJuego() {
                Torneo torneo1 = Torneo.builder()
                                .idJuego(5L)
                                .nombreTorneo("MSI 2025")
                                .nombreJuego("League of Legends")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(1))
                                .totalPremio(500000.0)
                                .maxEquipos(16)
                                .estado("PROGRAMADO")
                                .build();

                Torneo torneo2 = Torneo.builder()
                                .idJuego(5L)
                                .nombreTorneo("LEC Spring 2025")
                                .nombreJuego("League of Legends")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(3))
                                .totalPremio(200000.0)
                                .maxEquipos(10)
                                .estado("PROGRAMADO")
                                .build();

                torneoRepository.save(torneo1);
                torneoRepository.save(torneo2);

                List<Torneo> resultado = torneoRepository.findByIdJuego(5L);

                assertFalse(resultado.isEmpty());
                assertTrue(resultado.size() >= 2);
                assertEquals(5L, resultado.get(0).getIdJuego());
        }

        @Test
        void debeRetornarListaPorEstado() {
                Torneo torneo1 = Torneo.builder()
                                .idJuego(2L)
                                .nombreTorneo("VCT Masters Tokyo")
                                .nombreJuego("Valorant")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(1))
                                .totalPremio(300000.0)
                                .maxEquipos(12)
                                .estado("FINALIZADO")
                                .build();

                Torneo torneo2 = Torneo.builder()
                                .idJuego(2L)
                                .nombreTorneo("VCT Champions 2025")
                                .nombreJuego("Valorant")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(2))
                                .totalPremio(1000000.0)
                                .maxEquipos(16)
                                .estado("FINALIZADO")
                                .build();

                torneoRepository.save(torneo1);
                torneoRepository.save(torneo2);

                List<Torneo> resultado = torneoRepository.findByEstado("FINALIZADO");

                assertFalse(resultado.isEmpty());
                assertTrue(resultado.size() >= 2);
                assertEquals("FINALIZADO", resultado.get(0).getEstado());
        }

        @Test
        void debeRetornarListaPorOrganizador() {
                Torneo torneo1 = Torneo.builder()
                                .idJuego(1L)
                                .nombreTorneo("Game Changers EMEA 2025")
                                .nombreJuego("Valorant")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(2))
                                .totalPremio(150000.0)
                                .maxEquipos(8)
                                .estado("PROGRAMADO")
                                .build();

                Torneo torneo2 = Torneo.builder()
                                .idJuego(1L)
                                .nombreTorneo("Game Changers Americas 2025")
                                .nombreJuego("Valorant")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(3))
                                .totalPremio(150000.0)
                                .maxEquipos(8)
                                .estado("PROGRAMADO")
                                .build();

                torneoRepository.save(torneo1);
                torneoRepository.save(torneo2);

                List<Torneo> resultado = torneoRepository.findByOrganizador("Riot Games");

                assertFalse(resultado.isEmpty());
                assertTrue(resultado.size() >= 2);
                assertEquals("Riot Games", resultado.get(0).getOrganizador());
        }

        @Test
        void debeGuardarTorneo() {
                Torneo torneo = Torneo.builder()
                                .idJuego(1L)
                                .nombreTorneo("Worlds 2026")
                                .nombreJuego("League of Legends")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(2))
                                .totalPremio(2250000.0)
                                .maxEquipos(16)
                                .estado("PROGRAMADO")
                                .build();

                Torneo resultado = torneoRepository.save(torneo);

                assertEquals("Worlds 2026", resultado.getNombreTorneo());
                assertNotNull(resultado.getId());
        }

        @Test
        void debeListarTodosLosTorneos() {
                Torneo torneo1 = Torneo.builder()
                                .idJuego(1L)
                                .nombreTorneo("MSI 2026")
                                .nombreJuego("League of Legends")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(1))
                                .totalPremio(500000.0)
                                .maxEquipos(16)
                                .estado("PROGRAMADO")
                                .build();

                Torneo torneo2 = Torneo.builder()
                                .idJuego(2L)
                                .nombreTorneo("VCT Masters 2026")
                                .nombreJuego("Valorant")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(2))
                                .totalPremio(300000.0)
                                .maxEquipos(12)
                                .estado("PROGRAMADO")
                                .build();

                torneoRepository.save(torneo1);
                torneoRepository.save(torneo2);

                List<Torneo> resultado = torneoRepository.findAll();

                assertFalse(resultado.isEmpty());
                assertTrue(resultado.size() >= 2);
        }

        @Test
        void debeEliminarUnTorneo() {
                Torneo torneo = Torneo.builder()
                                .idJuego(2L)
                                .nombreTorneo("VCT Game Changers 2025")
                                .nombreJuego("Valorant")
                                .organizador("Riot Games")
                                .fechaInicio(LocalDate.now())
                                .fechaFin(LocalDate.now().plusMonths(2))
                                .totalPremio(100000.0)
                                .maxEquipos(8)
                                .estado("PROGRAMADO")
                                .build();

                Torneo guardado = torneoRepository.save(torneo);
                torneoRepository.delete(guardado);

                Optional<Torneo> resultado = torneoRepository.findById(guardado.getId());

                assertFalse(resultado.isPresent());
        }
}