package com.dentalcare.repository;

import com.dentalcare.entity.ArchivoClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ArchivoClinicoRepository extends JpaRepository<ArchivoClinico, Long>, JpaSpecificationExecutor<ArchivoClinico> {
    List<ArchivoClinico> findByPacienteId(Long pacienteId);

    List<ArchivoClinico> findByHistoriaClinicaId(Long historiaClinicaId);

    List<ArchivoClinico> findByTipoArchivo(String tipoArchivo);
}
