package com.dentalcare.repository;

import com.dentalcare.entity.HorarioAtencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Long> {
    List<HorarioAtencion> findByActivoTrueAndDiaSemana(DayOfWeek diaSemana);
}
