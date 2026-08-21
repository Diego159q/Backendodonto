package com.dentalcare.repository;

import com.dentalcare.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long>, JpaSpecificationExecutor<MovimientoInventario> {
    List<MovimientoInventario> findByProductoIdOrderByFechaDesc(Long productoId);

    List<MovimientoInventario> findByTipoMovimiento(String tipo);
}
