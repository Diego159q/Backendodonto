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
public class ArchivoClinicoRequest {
    @NotNull
    private Long pacienteId;

    private Long historiaClinicaId;

    @NotBlank
    private String tipoArchivo;

    private String descripcion;
}
