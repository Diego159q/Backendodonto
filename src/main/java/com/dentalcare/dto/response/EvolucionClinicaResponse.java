package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvolucionClinicaResponse {
    private Long id;
    private Long historiaClinicaId;
    private Long citaId;
    private Long odontologoId;
    private String odontologoNombre;
    private LocalDate fecha;
    private String descripcion;
    private String procedimientoRealizado;
    private String observaciones;
    private String recomendaciones;
    private LocalDateTime fechaCreacion;
}
