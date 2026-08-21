package com.dentalcare.repository;

import com.dentalcare.entity.BloqueoAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BloqueoAgendaRepository extends JpaRepository<BloqueoAgenda, Long> {
    List<BloqueoAgenda> findByFecha(LocalDate fecha);
}
