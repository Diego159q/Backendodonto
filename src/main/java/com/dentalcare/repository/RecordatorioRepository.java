package com.dentalcare.repository;

import com.dentalcare.entity.Recordatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordatorioRepository extends JpaRepository<Recordatorio, Long>, JpaSpecificationExecutor<Recordatorio> {
    List<Recordatorio> findByPacienteId(Long pacienteId);

    List<Recordatorio> findByEnviadoFalseAndFechaProgramadaBefore(LocalDateTime date);

    List<Recordatorio> findByTipo(String tipo);
}
