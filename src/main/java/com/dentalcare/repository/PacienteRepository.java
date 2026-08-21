package com.dentalcare.repository;

import com.dentalcare.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long>, JpaSpecificationExecutor<Paciente> {
    Optional<Paciente> findByDni(String dni);

    Optional<Paciente> findByCodigoPaciente(String codigo);

    List<Paciente> findByDniContaining(String dni);

    List<Paciente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombre, String apellido);

    @Query("SELECT p FROM Paciente p WHERE " +
           "(:nombres IS NULL OR LOWER(p.nombres) LIKE LOWER(CONCAT('%', :nombres, '%'))) AND " +
           "(:apellidos IS NULL OR LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :apellidos, '%'))) AND " +
           "(:dni IS NULL OR p.dni LIKE %:dni%) AND " +
           "(:telefono IS NULL OR p.telefono LIKE %:telefono%) AND " +
           "(:codigoPaciente IS NULL OR p.codigoPaciente LIKE %:codigoPaciente%)")
    List<Paciente> combinedSearch(@Param("nombres") String nombres,
                                   @Param("apellidos") String apellidos,
                                   @Param("dni") String dni,
                                   @Param("telefono") String telefono,
                                   @Param("codigoPaciente") String codigoPaciente);

    List<Paciente> findByActivoTrue();

    Optional<Paciente> findTopByOrderByIdDesc();
}
