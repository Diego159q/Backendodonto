package com.dentalcare.dto.request;

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
public class ReprogramarCitaRequest {
    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime horaInicio;

    @NotNull
    private LocalTime horaFin;

    private String motivoReprogramacion;
}
