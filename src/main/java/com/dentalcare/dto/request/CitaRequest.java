package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaRequest {
    @NotNull
    private Long pacienteId;

    @NotNull
    private Long odontologoId;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime horaInicio;

    @NotNull
    private LocalTime horaFin;

    @NotBlank
    private String motivo;

    private String tipoAtencion;

    private String consultorio;

    private String observaciones;
}
