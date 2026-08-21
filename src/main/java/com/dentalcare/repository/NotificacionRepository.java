package com.dentalcare.repository;

import com.dentalcare.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long>, JpaSpecificationExecutor<Notificacion> {
    List<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    List<Notificacion> findByLeidaFalseAndUsuarioId(Long usuarioId);

    long countByLeidaFalseAndUsuarioId(Long usuarioId);
}
