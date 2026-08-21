package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OdontogramaDetalleRequest {
    @NotNull
    private Integer numeroPieza;

    private String caraDental;

    @NotBlank
    private String condicion;

    private String descripcion;

    private String tratamientoPendiente;

    private String tratamientoRealizado;

    private String color;
}
