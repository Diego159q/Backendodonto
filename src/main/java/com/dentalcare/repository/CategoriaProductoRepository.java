package com.dentalcare.repository;

import com.dentalcare.entity.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long>, JpaSpecificationExecutor<CategoriaProducto> {
    Optional<CategoriaProducto> findByNombre(String nombre);

    List<CategoriaProducto> findByActivoTrue();
}
