package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentoResponse {
    private Long id;
    private String nombre;
    private String presentacion;
    private String concentracion;
    private String descripcion;
    private Boolean activo;
}
