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
public class PacienteTratamientoResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private Long diagnosticoId;
    private Long tratamientoId;
    private String tratamientoNombre;
    private Long odontologoId;
    private String odontologoNombre;
    private String piezaDental;
    private LocalDate fechaInicio;
    private LocalDate fechaFinEstimada;
    private LocalDate fechaFinReal;
    private BigDecimal precio;
    private BigDecimal descuento;
    private BigDecimal precioFinal;
    private Integer numeroSesiones;
    private Integer sesionesRealizadas;
    private Integer porcentajeAvance;
    private String estado;
    private String observaciones;
}
