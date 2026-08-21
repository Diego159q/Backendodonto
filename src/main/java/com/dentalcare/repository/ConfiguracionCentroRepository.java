package com.dentalcare.repository;

import com.dentalcare.entity.ConfiguracionCentro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConfiguracionCentroRepository extends JpaRepository<ConfiguracionCentro, Long>, JpaSpecificationExecutor<ConfiguracionCentro> {
}
