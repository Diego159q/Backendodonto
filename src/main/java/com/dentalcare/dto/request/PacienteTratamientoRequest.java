package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteTratamientoRequest {
    @NotNull
    private Long pacienteId;

    private Long diagnosticoId;

    @NotNull
    private Long odontologoId;

    @NotNull
    private Long tratamientoId;

    private String piezaDental;

    private LocalDate fechaInicio;

    private LocalDate fechaFinEstimada;

    @Positive
    private BigDecimal precio;

    @PositiveOrZero
    private BigDecimal descuento;

    @Positive
    private BigDecimal precioFinal;

    @Positive
    private Integer numeroSesiones;

    private Integer sesionesRealizadas;

    private String observaciones;
}
