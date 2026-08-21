package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private Long odontologoId;
    private String odontologoNombre;
    private String diagnostico;
    private LocalDate fecha;
    private String observaciones;
    private Boolean aprobada;
    private LocalDate fechaAprobacion;
    private Boolean activo;
    private List<RecetaDetalleResponse> detalles;
    private LocalDateTime fechaCreacion;
}
