package com.dentalcare.repository;

import com.dentalcare.entity.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface CuotaRepository extends JpaRepository<Cuota, Long>, JpaSpecificationExecutor<Cuota> {
    List<Cuota> findByPlanTratamientoId(Long planId);

    List<Cuota> findByEstado(String estado);

    List<Cuota> findByFechaVencimientoBeforeAndEstado(LocalDate date, String estado);
}
