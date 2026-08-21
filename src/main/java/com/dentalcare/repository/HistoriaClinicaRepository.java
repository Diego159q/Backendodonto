package com.dentalcare.repository;

import com.dentalcare.entity.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long>, JpaSpecificationExecutor<HistoriaClinica> {
    List<HistoriaClinica> findByPacienteIdOrderByFechaAperturaDesc(Long pacienteId);

    default List<HistoriaClinica> findByPacienteIdOrderByFechaCreacionDesc(Long pacienteId) {
        return findByPacienteIdOrderByFechaAperturaDesc(pacienteId);
    }

    List<HistoriaClinica> findByOdontologoResponsableId(Long odontologoId);
}
