package com.dentalcare.repository;

import com.dentalcare.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long>, JpaSpecificationExecutor<Pago> {
    List<Pago> findByPacienteId(Long pacienteId);

    List<Pago> findByPlanTratamientoId(Long planId);

    List<Pago> findByTratamientoId(Long tratamientoId);

    @Query("SELECT p FROM Pago p WHERE CAST(p.metodoPago AS string) = :metodoPago")
    List<Pago> findByMetodoPago(@Param("metodoPago") String metodoPago);

    Optional<Pago> findTopByOrderByNumeroPagoDesc();

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.fecha BETWEEN :start AND :end")
    BigDecimal sumMontosByFechaBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(p) FROM Pago p WHERE CAST(p.metodoPago AS string) = :metodo AND p.fecha BETWEEN :start AND :end")
    long countByMetodoPagoAndFechaBetween(@Param("metodo") String metodo,
                                           @Param("start") LocalDate start,
                                           @Param("end") LocalDate end);

    List<Pago> findByFechaBetween(LocalDate start, LocalDate end);

    @Query("SELECT p FROM Pago p WHERE CAST(p.estado AS string) = :estado")
    List<Pago> findByEstado(@Param("estado") String estado);
}
