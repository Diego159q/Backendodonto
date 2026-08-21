package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentoRequest {
    @NotBlank
    private String nombre;

    private String presentacion;

    private String concentracion;

    private String descripcion;

    private Boolean activo;
}
