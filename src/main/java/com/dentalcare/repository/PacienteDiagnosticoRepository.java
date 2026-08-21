package com.dentalcare.repository;

import com.dentalcare.entity.PacienteDiagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PacienteDiagnosticoRepository extends JpaRepository<PacienteDiagnostico, Long>, JpaSpecificationExecutor<PacienteDiagnostico> {
    List<PacienteDiagnostico> findByPacienteId(Long pacienteId);

    List<PacienteDiagnostico> findByDiagnosticoId(Long diagnosticoId);

    List<PacienteDiagnostico> findByEstado(String estado);
}
