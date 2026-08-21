package com.dentalcare.repository;

import com.dentalcare.entity.PlanTratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PlanTratamientoRepository extends JpaRepository<PlanTratamiento, Long>, JpaSpecificationExecutor<PlanTratamiento> {
    List<PlanTratamiento> findByPacienteIdOrderByFechaDesc(Long pacienteId);

    List<PlanTratamiento> findByEstado(String estado);

    List<PlanTratamiento> findByAceptadoPorPacienteFalse();
}
