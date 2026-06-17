package com.esports.ms_transferencias.repository;

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

import com.esports.ms_transferencias.model.Transferencia;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransferenciaRepositoryTest {

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Test
    void debeBuscarPorId() {
        Transferencia transferencia = new Transferencia();
        transferencia.setIdJugador(1L);
        transferencia.setNicknameJugador("Faker");
        transferencia.setTipo("FICHAJE_INICIAL");
        transferencia.setFechaTransferencia(LocalDate.now());

        Transferencia guardada = transferenciaRepository.save(transferencia);

        Optional<Transferencia> resultado = transferenciaRepository.findById(guardada.getId());

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdJugador());
        assertEquals("FICHAJE_INICIAL", resultado.get().getTipo());
    }

    @Test
    void debeRetornarListaPorJugador() {
        Transferencia t1 = new Transferencia();
        t1.setIdJugador(5L);
        t1.setNicknameJugador("Caps");
        t1.setTipo("FICHAJE_INICIAL");
        t1.setFechaTransferencia(LocalDate.now());

        Transferencia t2 = new Transferencia();
        t2.setIdJugador(5L);
        t2.setNicknameJugador("Caps");
        t2.setTipo("PRESTAMO");
        t2.setFechaTransferencia(LocalDate.now());

        transferenciaRepository.save(t1);
        transferenciaRepository.save(t2);

        List<Transferencia> resultado = transferenciaRepository.findByIdJugador(5L);

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeRetornarListaPorTipo() {
        Transferencia t1 = new Transferencia();
        t1.setIdJugador(10L);
        t1.setTipo("BAJA");
        t1.setFechaTransferencia(LocalDate.now());

        Transferencia t2 = new Transferencia();
        t2.setIdJugador(20L);
        t2.setTipo("BAJA");
        t2.setFechaTransferencia(LocalDate.now());

        transferenciaRepository.save(t1);
        transferenciaRepository.save(t2);

        List<Transferencia> resultado = transferenciaRepository.findByTipo("BAJA");

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeGuardarTransferencia() {
        Transferencia transferencia = new Transferencia();
        transferencia.setIdJugador(30L);
        transferencia.setNicknameJugador("Chovy");
        transferencia.setIdEquipoOrigen(1L);
        transferencia.setIdEquipoDestino(2L);
        transferencia.setTipo("TRANSFERENCIA");
        transferencia.setFechaTransferencia(LocalDate.now());

        Transferencia resultado = transferenciaRepository.save(transferencia);

        assertEquals("Chovy", resultado.getNicknameJugador());
        assertEquals("TRANSFERENCIA", resultado.getTipo());
        assertNotNull(resultado.getId());
    }

    @Test
    void debeListarTodasLasTransferencias() {
        Transferencia t1 = new Transferencia();
        t1.setIdJugador(1L);
        t1.setTipo("FICHAJE_INICIAL");
        t1.setFechaTransferencia(LocalDate.now());

        Transferencia t2 = new Transferencia();
        t2.setIdJugador(2L);
        t2.setTipo("BAJA");
        t2.setFechaTransferencia(LocalDate.now());

        transferenciaRepository.save(t1);
        transferenciaRepository.save(t2);

        List<Transferencia> resultado = transferenciaRepository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarUnaTransferencia() {
        Transferencia transferencia = new Transferencia();
        transferencia.setIdJugador(100L);
        transferencia.setTipo("PRESTAMO");
        transferencia.setFechaTransferencia(LocalDate.now());

        Transferencia guardada = transferenciaRepository.save(transferencia);
        transferenciaRepository.delete(guardada);

        Optional<Transferencia> resultado = transferenciaRepository.findById(guardada.getId());

        assertFalse(resultado.isPresent());
    }
}