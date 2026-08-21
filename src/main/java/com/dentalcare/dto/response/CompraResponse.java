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
public class CompraResponse {
    private Long id;
    private Long proveedorId;
    private String proveedorNombre;
    private LocalDate fecha;
    private String numeroDocumento;
    private BigDecimal montoTotal;
    private String estado;
    private Long usuarioRegistroId;
    private String usuarioRegistroNombre;
    private LocalDateTime fechaCreacion;
}
