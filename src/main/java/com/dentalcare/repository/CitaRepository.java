package com.dentalcare.repository;

import com.dentalcare.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long>, JpaSpecificationExecutor<Cita> {
    List<Cita> findByFecha(LocalDate fecha);

    List<Cita> findByFechaBetween(LocalDate start, LocalDate end);

    List<Cita> findByPacienteId(Long pacienteId);

    List<Cita> findByOdontologoId(Long odontologoId);

    @Query("SELECT c FROM Cita c WHERE CAST(c.estado AS string) = :estado")
    List<Cita> findByEstado(@Param("estado") String estado);

    @Query("SELECT c FROM Cita c WHERE c.odontologo.id = :odontologoId AND c.fecha = :fecha " +
           "AND c.horaInicio < :horaFin AND c.horaFin > :horaInicio")
    List<Cita> findOverlappingCitas(@Param("odontologoId") Long odontologoId,
                                     @Param("fecha") LocalDate fecha,
                                     @Param("horaInicio") LocalTime horaInicio,
                                     @Param("horaFin") LocalTime horaFin);

    long countByFecha(LocalDate fecha);

    @Query("SELECT COUNT(c) FROM Cita c WHERE CAST(c.estado AS string) = :estado AND c.fecha BETWEEN :start AND :end")
    long countByEstadoAndFechaBetween(@Param("estado") String estado,
                                       @Param("start") LocalDate start,
                                       @Param("end") LocalDate end);

    List<Cita> findByFechaAndOdontologoIdOrderByHoraInicio(LocalDate fecha, Long odontologoId);

    List<Cita> findByPacienteIdOrderByFechaDesc(Long pacienteId);
}
