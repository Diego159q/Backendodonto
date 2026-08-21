package com.dentalcare.dto.response;

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
public class CuotaResponse {
    private Long id;
    private Long planTratamientoId;
    private Integer numeroCuota;
    private BigDecimal monto;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private String estado;
    private Long pagoRelacionadoId;
}
