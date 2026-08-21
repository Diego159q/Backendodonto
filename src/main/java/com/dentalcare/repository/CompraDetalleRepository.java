package com.dentalcare.repository;

import com.dentalcare.entity.CompraDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CompraDetalleRepository extends JpaRepository<CompraDetalle, Long>, JpaSpecificationExecutor<CompraDetalle> {
    List<CompraDetalle> findByCompraId(Long compraId);
}
