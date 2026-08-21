package com.dentalcare.repository;

import com.dentalcare.entity.PlanTratamientoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PlanTratamientoDetalleRepository extends JpaRepository<PlanTratamientoDetalle, Long>, JpaSpecificationExecutor<PlanTratamientoDetalle> {
    List<PlanTratamientoDetalle> findByPlanTratamientoId(Long planTratamientoId);

    default List<PlanTratamientoDetalle> findByPlanId(Long planId) {
        return findByPlanTratamientoId(planId);
    }
}