package com.dentalcare.repository;

import com.dentalcare.entity.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OdontologoRepository extends JpaRepository<Odontologo, Long>, JpaSpecificationExecutor<Odontologo> {
    Optional<Odontologo> findByUsuarioId(Long usuarioId);

    Optional<Odontologo> findByNumeroColegiatura(String colegiatura);

    List<Odontologo> findByActivoTrue();

    Optional<Odontologo> findByEmail(String email);
}
