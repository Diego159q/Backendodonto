package com.dentalcare.repository;

import com.dentalcare.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long>, JpaSpecificationExecutor<Compra> {
    List<Compra> findByProveedorId(Long proveedorId);

    List<Compra> findByFechaBetween(LocalDate start, LocalDate end);
}
