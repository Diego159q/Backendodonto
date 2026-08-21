package com.dentalcare.repository;

import com.dentalcare.entity.OdontogramaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OdontogramaDetalleRepository extends JpaRepository<OdontogramaDetalle, Long>, JpaSpecificationExecutor<OdontogramaDetalle> {
    List<OdontogramaDetalle> findByOdontogramaId(Long odontogramaId);

    List<OdontogramaDetalle> findByOdontogramaIdAndNumeroPieza(Long odontogramaId, Integer numeroPieza);
}
