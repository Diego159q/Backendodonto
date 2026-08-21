package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteDiagnosticoRequest {
    @NotNull
    private Long pacienteId;

    @NotNull
    private Long diagnosticoId;

    @NotNull
    private Long odontologoId;

    private String piezaDental;

    private LocalDate fecha;

    private String estado;

    private String observaciones;
}
