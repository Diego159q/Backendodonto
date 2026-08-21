package com.dentalcare.repository;

import com.dentalcare.entity.EvolucionClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EvolucionClinicaRepository extends JpaRepository<EvolucionClinica, Long>, JpaSpecificationExecutor<EvolucionClinica> {
    List<EvolucionClinica> findByHistoriaClinicaIdOrderByFechaDesc(Long historiaClinicaId);

    List<EvolucionClinica> findByHistoriaClinicaId(Long historiaClinicaId);

    List<EvolucionClinica> findByCitaId(Long citaId);
}
