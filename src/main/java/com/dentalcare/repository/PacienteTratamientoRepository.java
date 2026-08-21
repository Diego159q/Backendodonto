package com.dentalcare.repository;

import com.dentalcare.entity.EstadoTratamiento;
import com.dentalcare.entity.PacienteTratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PacienteTratamientoRepository extends JpaRepository<PacienteTratamiento, Long>, JpaSpecificationExecutor<PacienteTratamiento> {
    List<PacienteTratamiento> findByPacienteId(Long pacienteId);

    List<PacienteTratamiento> findByOdontologoId(Long odontologoId);

    List<PacienteTratamiento> findByEstado(EstadoTratamiento estado);

    long countByEstado(EstadoTratamiento estado);

    List<PacienteTratamiento> findByPacienteIdAndEstado(Long pacienteId, EstadoTratamiento estado);

    List<PacienteTratamiento> findByTratamientoIdAndPacienteId(Long tratamientoId, Long pacienteId);
}
