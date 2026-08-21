package com.dentalcare.repository;

import com.dentalcare.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RecetaRepository extends JpaRepository<Receta, Long>, JpaSpecificationExecutor<Receta> {
    List<Receta> findByPacienteIdOrderByFechaDesc(Long pacienteId);

    List<Receta> findByPacienteId(Long pacienteId);

    List<Receta> findByOdontologoId(Long odontologoId);

    List<Receta> findByAprobadaFalse();
}
