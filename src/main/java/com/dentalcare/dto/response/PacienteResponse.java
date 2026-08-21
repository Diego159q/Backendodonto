package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteResponse {
    private Long id;
    private String codigoPaciente;
    private String nombres;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private Integer edad;
    private String sexo;
    private String telefono;
    private String email;
    private String direccion;
    private String distrito;
    private String ciudad;
    private String estadoCivil;
    private String ocupacion;
    private String tipoSangre;
    private String alergias;
    private String enfermedadesPrevias;
    private String medicamentosActuales;
    private String contactoEmergencia;
    private String telefonoEmergencia;
    private String observaciones;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaCreacion;
}
