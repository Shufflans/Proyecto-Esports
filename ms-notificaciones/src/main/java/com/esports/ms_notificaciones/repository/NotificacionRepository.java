package com.esports.ms_notificaciones.repository;

import com.esports.ms_notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByIdJugador(Long idJugador);
}