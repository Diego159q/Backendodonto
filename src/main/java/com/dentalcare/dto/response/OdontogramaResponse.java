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
public class OdontogramaResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private Long odontologoId;
    private String odontologoNombre;
    private LocalDate fecha;
    private String tipoDenticion;
    private String observaciones;
    private String estado;
    private Boolean activo;
    private List<OdontogramaDetalleResponse> detalles;
    private LocalDateTime fechaCreacion;
}
