package com.dentalcare.repository;

import com.dentalcare.entity.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long>, JpaSpecificationExecutor<Diagnostico> {
    Optional<Diagnostico> findByCodigo(String codigo);

    List<Diagnostico> findByNombreContainingIgnoreCase(String nombre);

    List<Diagnostico> findByActivoTrue();
}
