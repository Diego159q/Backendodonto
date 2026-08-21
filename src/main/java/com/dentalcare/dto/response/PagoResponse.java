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
public class PagoResponse {
    private Long id;
    private String numeroPago;
    private Long pacienteId;
    private String pacienteNombre;
    private Long planTratamientoId;
    private Long tratamientoId;
    private BigDecimal monto;
    private LocalDate fecha;
    private String metodoPago;
    private String numeroOperacion;
    private String observaciones;
    private Long usuarioRegistroId;
    private String usuarioRegistroNombre;
    private String estado;
    private LocalDateTime fechaCreacion;
}
