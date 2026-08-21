package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TratamientoRequest {
    @NotBlank
    private String nombre;

    private String descripcion;

    @Positive
    private BigDecimal precioBase;

    @Positive
    private Integer numeroSesiones;

    private Boolean activo;
}
