package com.dentalcare.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PublicReservaRequest {
    
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;
    
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;
    
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
    
    @Email(message = "Email inválido")
    private String email;
    
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    
    @NotNull(message = "La hora es obligatoria")
    private LocalTime horaInicio;
    
    @NotNull(message = "El tratamiento (servicio) es obligatorio")
    private Long tratamientoId;
}
