package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class ProductoRequest {
    @NotBlank
    private String codigo;

    @NotBlank
    private String nombre;

    private Long categoriaId;

    private String descripcion;

    private String unidadMedida;

    @PositiveOrZero
    private Integer stockActual;

    @PositiveOrZero
    private Integer stockMinimo;

    @PositiveOrZero
    private BigDecimal precioCompra;

    @PositiveOrZero
    private BigDecimal precioVenta;

    private LocalDate fechaVencimiento;

    private Long proveedorId;

    private String lote;

    private Boolean activo;
}
