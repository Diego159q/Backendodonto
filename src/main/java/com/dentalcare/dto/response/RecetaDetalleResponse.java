package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaDetalleResponse {
    private Long id;
    private Long recetaId;
    private Long medicamentoId;
    private String medicamentoNombre;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private String indicaciones;
    private Integer orden;
}
