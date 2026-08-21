package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaRequest {
    @NotNull
    private Long pacienteId;

    @NotNull
    private Long odontologoId;

    private String diagnostico;

    private String observaciones;

    private List<RecetaDetalleRequest> medicamentos;
}
