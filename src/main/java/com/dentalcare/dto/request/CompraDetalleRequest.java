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
public class CompraDetalleRequest {
    @NotNull
    private Long productoId;

    @NotNull
    @Positive
    private Integer cantidad;

    @NotNull
    @Positive
    private BigDecimal precioUnitario;

    private String lote;

    private LocalDate fechaVencimiento;
}
