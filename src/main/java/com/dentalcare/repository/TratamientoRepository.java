package com.dentalcare.repository;

import com.dentalcare.entity.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TratamientoRepository extends JpaRepository<Tratamiento, Long>, JpaSpecificationExecutor<Tratamiento> {
    List<Tratamiento> findByNombreContainingIgnoreCase(String nombre);

    List<Tratamiento> findByActivoTrue();
}
