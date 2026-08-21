package com.dentalcare.repository;

import com.dentalcare.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long>, JpaSpecificationExecutor<Medicamento> {
    List<Medicamento> findByNombreContainingIgnoreCase(String nombre);

    List<Medicamento> findByActivoTrue();
}
