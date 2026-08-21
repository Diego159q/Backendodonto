package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventarioRequest {
    @NotNull
    private Long productoId;

    @NotBlank
    private String tipoMovimiento;

    @NotNull
    private Integer cantidad;

    private String motivo;
}
