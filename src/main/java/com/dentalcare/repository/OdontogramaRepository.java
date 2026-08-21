package com.dentalcare.repository;

import com.dentalcare.entity.Odontograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OdontogramaRepository extends JpaRepository<Odontograma, Long>, JpaSpecificationExecutor<Odontograma> {
    List<Odontograma> findByPacienteIdAndActivoTrue(Long pacienteId);

    List<Odontograma> findByPacienteId(Long pacienteId);

    Optional<Odontograma> findTopByPacienteIdAndActivoTrueOrderByFechaDesc(Long pacienteId);
}
