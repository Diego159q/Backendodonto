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
public class OdontogramaDetalleResponse {
    private Long id;
    private Long odontogramaId;
    private Integer numeroPieza;
    private String caraDental;
    private String condicion;
    private String descripcion;
    private String tratamientoPendiente;
    private String tratamientoRealizado;
    private String color;
    private LocalDateTime fechaRegistro;
}
