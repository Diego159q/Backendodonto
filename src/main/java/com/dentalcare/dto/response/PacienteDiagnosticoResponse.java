package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteDiagnosticoResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private Long diagnosticoId;
    private String diagnosticoNombre;
    private Long odontologoId;
    private String odontologoNombre;
    private String piezaDental;
    private LocalDate fecha;
    private String estado;
    private String observaciones;
}
