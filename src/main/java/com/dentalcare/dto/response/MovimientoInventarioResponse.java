package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventarioResponse {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private String tipoMovimiento;
    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private String motivo;
    private LocalDateTime fecha;
    private Long usuarioRegistroId;
    private String usuarioRegistroNombre;
}
