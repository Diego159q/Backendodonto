package com.dentalcare.repository;

import com.dentalcare.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long>, JpaSpecificationExecutor<Rol> {
    Optional<Rol> findByNombre(String nombre);

    List<Rol> findByActivoTrue();
}
