package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanTratamientoResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private Long odontologoId;
    private String odontologoNombre;
    private LocalDate fecha;
    private BigDecimal montoTotal;
    private BigDecimal descuentoTotal;
    private BigDecimal montoFinal;
    private BigDecimal adelanto;
    private BigDecimal saldo;
    private String estado;
    private Boolean aceptadoPorPaciente;
    private LocalDate fechaAceptacion;
    private String observaciones;
    private List<PlanTratamientoDetalleResponse> detalles;
    private LocalDateTime fechaCreacion;
}
