package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class PagoRequest {
    @NotNull
    private Long pacienteId;

    private Long planTratamientoId;

    private Long tratamientoId;

    @NotNull
    @Positive
    private BigDecimal monto;

    private LocalDate fecha;

    @NotBlank
    private String metodoPago;

    private String numeroOperacion;

    private String observaciones;
}
