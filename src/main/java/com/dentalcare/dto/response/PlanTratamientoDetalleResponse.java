package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanTratamientoDetalleResponse {
    private Long id;
    private Long planTratamientoId;
    private Long tratamientoId;
    private String tratamientoNombre;
    private String piezaDental;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
    private String estado;
    private Integer numeroSesiones;
}
