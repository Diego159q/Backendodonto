package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanTratamientoDetalleRequest {
    @NotNull
    private Long pacienteTratamientoId;

    private String piezaDental;

    @Positive
    private Integer cantidad;

    @Positive
    private BigDecimal precioUnitario;

    @PositiveOrZero
    private BigDecimal descuento;

    @Positive
    private Integer numeroSesiones;
}
