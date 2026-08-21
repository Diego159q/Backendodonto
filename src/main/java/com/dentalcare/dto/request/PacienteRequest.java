package com.dentalcare.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteRequest {
    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotBlank
    private String dni;

    @Past
    private LocalDate fechaNacimiento;

    private String sexo;

    private String telefono;

    @Email
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
}
