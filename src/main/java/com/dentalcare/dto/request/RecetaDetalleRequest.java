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
public class RecetaDetalleRequest {
    @NotNull
    private Long medicamentoId;

    @NotBlank
    private String dosis;

    @NotBlank
    private String frecuencia;

    @NotBlank
    private String duracion;

    private String indicaciones;

    private Integer orden;
}
