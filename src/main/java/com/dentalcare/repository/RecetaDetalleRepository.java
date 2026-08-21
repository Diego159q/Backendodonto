package com.dentalcare.repository;

import com.dentalcare.entity.RecetaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RecetaDetalleRepository extends JpaRepository<RecetaDetalle, Long>, JpaSpecificationExecutor<RecetaDetalle> {
    List<RecetaDetalle> findByRecetaId(Long recetaId);
}
