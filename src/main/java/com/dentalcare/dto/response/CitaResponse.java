package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private Long odontologoId;
    private String odontologoNombre;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String motivo;
    private String tipoAtencion;
    private String consultorio;
    private String estado;
    private String observaciones;
    private String motivoCancelacion;
    private LocalDateTime fechaCreacion;
}
