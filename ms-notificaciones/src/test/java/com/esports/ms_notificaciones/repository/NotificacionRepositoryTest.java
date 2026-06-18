package com.esports.ms_notificaciones.repository;

import com.esports.ms_notificaciones.model.Notificacion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NotificacionRepositoryTest {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Test
    void debeBuscarPorId() {
        Notificacion notificacion = Notificacion.builder()
                .idJugador(1L)
                .nombreJugador("Shufflan")
                .asunto("Bienvenido")
                .mensaje("Hola, bienvenido al torneo")
                .estado("NO_LEIDA")
                .fechaCreacion(LocalDateTime.now())
                .build();
        Notificacion guardada = notificacionRepository.save(notificacion);

        Optional<Notificacion> resultado = notificacionRepository.findById(guardada.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Bienvenido", resultado.get().getAsunto());
        assertEquals("NO_LEIDA", resultado.get().getEstado());
    }

    @Test
    void debeRetornarListaPorIdJugador() {
        Notificacion notificacion1 = Notificacion.builder()
                .idJugador(5L)
                .nombreJugador("nakaseomyces")
                .asunto("Aviso de Partido")
                .mensaje("Tienes un partido mañana")
                .estado("NO_LEIDA")
                .fechaCreacion(LocalDateTime.now())
                .build();

        Notificacion notificacion2 = Notificacion.builder()
                .idJugador(5L)
                .nombreJugador("nakaseomyces")
                .asunto("Sanción")
                .mensaje("Sanción por inactividad")
                .estado("LEIDA")
                .fechaCreacion(LocalDateTime.now())
                .build();

        notificacionRepository.save(notificacion1);
        notificacionRepository.save(notificacion2);

        List<Notificacion> resultado = notificacionRepository.findByIdJugador(5L);

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
        assertEquals(5L, resultado.get(0).getIdJugador());
    }

    @Test
    void debeGuardarNotificacion() {
        Notificacion notificacion = Notificacion.builder()
                .idJugador(2L)
                .nombreJugador("Camilo")
                .asunto("Cambio de equipo")
                .mensaje("Has sido transferido")
                .estado("NO_LEIDA")
                .fechaCreacion(LocalDateTime.now())
                .build();

        Notificacion resultado = notificacionRepository.save(notificacion);

        assertNotNull(resultado.getId());
        assertEquals("Cambio de equipo", resultado.getAsunto());
    }

    @Test
    void debeListarTodasLasNotificaciones() {
        Notificacion notificacion1 = Notificacion.builder()
                .idJugador(1L)
                .nombreJugador("Shufflan")
                .asunto("Aviso 1")
                .mensaje("Mensaje 1")
                .estado("NO_LEIDA")
                .fechaCreacion(LocalDateTime.now())
                .build();

        Notificacion notificacion2 = Notificacion.builder()
                .idJugador(2L)
                .nombreJugador("nakaseomyces")
                .asunto("Aviso 2")
                .mensaje("Mensaje 2")
                .estado("LEIDA")
                .fechaCreacion(LocalDateTime.now())
                .build();

        notificacionRepository.save(notificacion1);
        notificacionRepository.save(notificacion2);

        List<Notificacion> resultado = notificacionRepository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarUnaNotificacion() {
        Notificacion notificacion = Notificacion.builder()
                .idJugador(1L)
                .nombreJugador("Shufflan")
                .asunto("Aviso temporal")
                .mensaje("Este aviso se borrará")
                .estado("NO_LEIDA")
                .fechaCreacion(LocalDateTime.now())
                .build();

        Notificacion guardada = notificacionRepository.save(notificacion);
        notificacionRepository.delete(guardada);

        Optional<Notificacion> resultado = notificacionRepository.findById(guardada.getId());

        assertFalse(resultado.isPresent());
    }
}