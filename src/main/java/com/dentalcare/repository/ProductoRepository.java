package com.dentalcare.repository;

import com.dentalcare.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    Optional<Producto> findByCodigo(String codigo);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByProveedorId(Long proveedorId);

    List<Producto> findByStockActualLessThanEqual(Integer stock);

    List<Producto> findByFechaVencimientoBetween(LocalDate start, LocalDate end);

    List<Producto> findByFechaVencimientoBefore(LocalDate date);

    List<Producto> findByActivoTrue();
}
