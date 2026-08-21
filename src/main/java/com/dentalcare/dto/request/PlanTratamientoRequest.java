package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotNull;
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
public class PlanTratamientoRequest {
    @NotNull
    private Long pacienteId;

    @NotNull
    private Long odontologoId;

    @PositiveOrZero
    private BigDecimal descuentoTotal;

    @PositiveOrZero
    private BigDecimal adelanto;

    private String observaciones;
}
