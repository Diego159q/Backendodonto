package com.dentalcare.repository;

import com.dentalcare.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long>, JpaSpecificationExecutor<Auditoria> {
    List<Auditoria> findByUsuarioId(Long usuarioId);

    List<Auditoria> findByFechaBetween(LocalDateTime start, LocalDateTime end);
}
