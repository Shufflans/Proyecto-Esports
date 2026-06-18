package com.esports.ms_patrocinadores.repository;

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

import com.esports.ms_patrocinadores.model.Patrocinador;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PatrocinadorRepositoryTest {

    @Autowired
    private PatrocinadorRepository patrocinadorRepository;

    @Test
    void debeBuscarPorId() {
        Patrocinador patrocinador = Patrocinador.builder()
                .nombreMarca("Red Bull")
                .idEquipo(10L)
                .montoAnual(50000.0)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .build();

        Patrocinador guardado = patrocinadorRepository.save(patrocinador);

        Optional<Patrocinador> resultado = patrocinadorRepository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Red Bull", resultado.get().getNombreMarca());
        assertEquals(10L, resultado.get().getIdEquipo());
    }

    @Test
    void debeRetornarListaPorEquipo() {
        Patrocinador patrocinador1 = Patrocinador.builder()
                .nombreMarca("Logitech")
                .idEquipo(15L)
                .montoAnual(25000.0)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(2))
                .build();

        Patrocinador patrocinador2 = Patrocinador.builder()
                .nombreMarca("Intel")
                .idEquipo(15L)
                .montoAnual(40000.0)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .build();

        patrocinadorRepository.save(patrocinador1);
        patrocinadorRepository.save(patrocinador2);

        List<Patrocinador> resultado = patrocinadorRepository.findByIdEquipo(15L);

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeGuardarPatrocinador() {
        Patrocinador patrocinador = Patrocinador.builder()
                .nombreMarca("Monster Energy")
                .idEquipo(25L)
                .montoAnual(30000.0)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(3))
                .build();

        Patrocinador resultado = patrocinadorRepository.save(patrocinador);

        assertEquals("Monster Energy", resultado.getNombreMarca());
        assertNotNull(resultado.getId());
    }

    @Test
    void debeListarTodosLosPatrocinadores() {
        Patrocinador patrocinador1 = Patrocinador.builder()
                .nombreMarca("Secretlab")
                .idEquipo(10L)
                .montoAnual(15000.0)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .build();

        Patrocinador patrocinador2 = Patrocinador.builder()
                .nombreMarca("Corsair")
                .idEquipo(20L)
                .montoAnual(20000.0)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .build();

        patrocinadorRepository.save(patrocinador1);
        patrocinadorRepository.save(patrocinador2);

        List<Patrocinador> resultado = patrocinadorRepository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarUnPatrocinador() {
        Patrocinador patrocinador = Patrocinador.builder()
                .nombreMarca("Razer")
                .idEquipo(10L)
                .montoAnual(35000.0)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(2))
                .build();

        Patrocinador guardado = patrocinadorRepository.save(patrocinador);

        patrocinadorRepository.delete(guardado);

        Optional<Patrocinador> resultado = patrocinadorRepository.findById(guardado.getId());

        assertFalse(resultado.isPresent());
    }
}