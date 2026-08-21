package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private Long categoriaId;
    private String categoriaNombre;
    private String descripcion;
    private String unidadMedida;
    private Integer stockActual;
    private Integer stockMinimo;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private LocalDate fechaVencimiento;
    private String lote;
    private Long proveedorId;
    private String proveedorNombre;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
