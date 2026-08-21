package com.dentalcare.repository;

import com.dentalcare.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long>, JpaSpecificationExecutor<Proveedor> {
    Optional<Proveedor> findByRuc(String ruc);

    List<Proveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);

    List<Proveedor> findByActivoTrue();
}
