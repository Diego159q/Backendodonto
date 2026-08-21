package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CuotaRequest {
    @NotNull
    private Long planTratamientoId;

    @Positive
    private Integer numeroCuota;

    @NotNull
    @Positive
    private BigDecimal monto;

    @NotNull
    private LocalDate fechaVencimiento;
}
